package com.project.security;

import com.project.security.controllers.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User; // Spring's User
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private Data data; // We inject your existing Data class

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Iterate through your users list to find the matching email
        // Your User class is com.mail.demo.controllers.User
        com.project.security.controllers.User user = null;

        for (com.project.security.controllers.User u : data.getUsers()) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // 2. Convert your User object into a Spring Security User object
        return User.builder()
                .username(user.getEmail()) // We use email as the username
                .password(user.getPassword()) // Pass the password from JSON
                .roles(user.getRole()) // Assign a default role since your User class doesn't have one
                .build();
    }
}
