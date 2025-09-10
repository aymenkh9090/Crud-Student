package com.crud_app.service.impl;

import com.crud_app.model.ERole;
import com.crud_app.model.Role;
import com.crud_app.model.User;
import com.crud_app.repository.RoleRepository;
import com.crud_app.repository.UserRepository;
import com.crud_app.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public User registerUser(User userDto, ERole role) {
        // verification sii utilisateur deja existe
        if(userRepository.existsByUserName(userDto.getUserName())) {
            throw new RuntimeException("Username is already in use");
        }
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        // verification si role est disponible
        Role userRole = roleRepository.findByRoleName(role)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setEnabled(false);
        user.setActivationToken(UUID.randomUUID().toString());

        User registeredUser = userRepository.save(user);
        sendActivationEmail(registeredUser);
        return registeredUser;
    }

    /*@Override
        @Transactional
        public User registerUser(User userDto, ERole role) {

            //check si user deje existe
            if(userRepository.existsByUserName(userDto.getUserName())) {
                throw new RuntimeException("User name already exists");
            }
            //check si le role est disponible
            Role userRole = roleRepository.findByRoleName(role)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            User user = new User();
            user.setUserName(userDto.getUserName());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);
            return savedUser;

        }
    */
    @Override
    public void sendActivationEmail(User user) {

        //1-preparation de URL d'activation
        String activationUrl ="http://localhost:8090/activate?token="+user.getActivationToken();
        //2-preparation de message a envoyé
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Activation Email");
        message.setText("Pour activer votre compte..Clique sur ce lien au dessous : \n "+activationUrl);
        mailSender.send(message);
        System.out.println("Activation Email envoyé : pour  " + user.getEmail() + " avec le lien : " + activationUrl);
    }

    @Override
    @Transactional
    public boolean activateUserAccount(String token) {

        Optional<User> optionalUser =
                userRepository.findByActivationToken(token);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setActivationToken(null); // on supprime la cle d'activation
            userRepository.save(user);
            return true;
        }else {
            return false;
        }

    }
}
