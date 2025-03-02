package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LogInController {
    @Autowired
    public UserService userService;

    @PostMapping
    public String login(@RequestBody User user){
        return userService.checkUser(user);
    }
}
