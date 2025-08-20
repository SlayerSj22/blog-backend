package com.shashwat.Blog.service;

import com.shashwat.Blog.model.CustomUserDetails;
import com.shashwat.Blog.model.User;
import com.shashwat.Blog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found");

        // Return your CustomUserDetails instead of Spring's User
        return CustomUserDetails.fromUser(user);
    }
}

