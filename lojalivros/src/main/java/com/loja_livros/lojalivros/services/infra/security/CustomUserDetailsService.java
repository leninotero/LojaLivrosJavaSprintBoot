package com.loja_livros.lojalivros.services.infra.security;

import com.loja_livros.lojalivros.models.UserModel;
import com.loja_livros.lojalivros.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    // This service can be used to load user-specific data, such as roles and permissions.
    // It can be extended to implement methods for loading user details from the database or other sources.
    @Autowired
    private UserRepository userRepository;

     // This method is called by Spring Security to load user details by username.
     // It can be used to retrieve user information from the database or any other source.
     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         // Logic to retrieve user details from the database
        UserModel userModel =this.userRepository.findByEmail(username).orElseThrow(
                                                () -> new UsernameNotFoundException("User not found: " + username));
        return new User(
                userModel.getEmail(),
                userModel.getPassword(),
                new ArrayList<>()
        );
     }
}
