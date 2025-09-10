package com.crud_app.service.impl;

import com.crud_app.model.User;
import com.crud_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //-1- Chercher l'utilisateur avec son userName
        User user =
                userRepository.findByUserName(username)
                        .orElseThrow(()->new UsernameNotFoundException("User : " + username + " not found"));
        //-2- Convertir les roles de l'utilisateur en GrantedAuthority connaitre par spring
        Set<GrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role->new SimpleGrantedAuthority(role.getRoleName().name()))
                        .collect(Collectors.toSet());
        //-3-Cree un objet userDetails avec les informations d'utilisateurs
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
