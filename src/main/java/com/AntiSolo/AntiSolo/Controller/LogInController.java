package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Services.UserDetailsServiceImpl;
import com.AntiSolo.AntiSolo.Services.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

@RestController
@RequestMapping("/login")
//@CrossOrigin
public class LogInController {

//    private final Bucket postBucket = Bucket.builder()
//            .addLimit(Bandwidth.classic(2, Refill.intervally(1, Duration.ofMinutes(1))))
//            .build();


    @Autowired
    public UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping("/searchUser/{username}")
    public List<User> searchUsers(@PathVariable String username){
        return userService.findUsersWithPrefix(username);
//        List<User> users = userService.getById(username);
//        if(user.isPresent())return new ResponseEntity<>(user.get(),HttpStatus.OK);
//        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/userInfo/{username}")
    public ResponseEntity<User> getUserInfo(@PathVariable String username){
        Optional<User> user = userService.getById(username);
        if(user.isPresent())return new ResponseEntity<>(user.get(),HttpStatus.OK);
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody User user){

//        ConsumptionProbe probe = postBucket.tryConsumeAndReturnRemaining(1);
//        if (!probe.isConsumed()) {
//            return new ResponseEntity<>("Rate limit exceeded. Please try again later.", HttpStatus.TOO_MANY_REQUESTS);
//        }

        this.doAuthenticate(user.getUserName(),user.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        String token = this.jwtHelper.generateToken(userDetails);
//        return userService.checkUser(user);
        return new ResponseEntity<>(token,HttpStatus.OK);
    }
    @GetMapping("/checkToken/{token}")
    public boolean check(@PathVariable String token){
        return !jwtHelper.isTokenExpired(token);
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }


}
