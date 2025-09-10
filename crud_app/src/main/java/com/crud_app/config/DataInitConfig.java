package com.crud_app.config;

import com.crud_app.model.ERole;
import com.crud_app.model.Role;
import com.crud_app.model.User;
import com.crud_app.repository.RoleRepository;
import com.crud_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    //@Bean
    CommandLineRunner initData(){
        return args -> {
            //1-Creation des Roles
            Role adminRole = new Role(ERole.ROLE_ADMIN);
            Role userRole = new Role(ERole.ROLE_USER);
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            //2-Creations des users
            User admin = User.builder()
                    .userName("aymen")
                    .password(passwordEncoder.encode("123456"))
                    .enabled(true)
                    .roles(Set.of(adminRole,userRole))
                    .build();
            User user = User.builder()
                    .userName("khouloud")
                    .password(passwordEncoder.encode("123456"))
                    .enabled(true)
                    .roles(Set.of(userRole))
                    .build();
            userRepository.save(admin);
            userRepository.save(user);
            System.out.println(  "Admin: " + admin + " user: " + user + "a etait enregistre avec succes");

        };
    }

}
