package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<User> user = userService.getById(username);
        if(user.isPresent()){
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.get().getUserName()).password(user.get().getPassword()).roles("USER").build();
            return userDetails;
        }
        throw new UsernameNotFoundException("User not found with username "+username);
    }
}
