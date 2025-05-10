package com.AntiSolo.AntiSolo.Controller;


import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.OTP;
import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Services.EmailService;
import com.AntiSolo.AntiSolo.Services.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/signUp")
//@CrossOrigin
public class SignUpController {
    private final Map<String, Bucket> otpBuckets = new ConcurrentHashMap<>();



    @Autowired
    public EmailService emailService;

    @Autowired
    public UserService userService;


    public Bucket resolveOtpBucket(String key) {
        return otpBuckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofDays(1))))
                        .build()
        );
    }

    @PostMapping("/saveUser/{otp}")
    public String saveUser(@PathVariable String otp, @RequestBody User user){
//
//        System.out.println("opt = "+otp+" user = "+user+" inside controller "+otp);

//        ConsumptionProbe probe = postBucket.tryConsumeAndReturnRemaining(1);
//        if (!probe.isConsumed()) {
//            return "Rate limit exceeded. Please try again later.";
//        }

        return userService.saveEntry(user,otp,true);
    }

    @PostMapping("/editPassword/{otp}")
    public ResponseEntity<String> editPassword(@PathVariable String otp,@RequestBody User user){
        String res = userService.saveEntry(user,otp,false);
        if(res.equals("Incorrect OTP") || res.equals("Password cannot be empty") || res.equals("Password cannot contain spaces")
        || res.equals("Password length must be between 4 to 20 letters") || res.equals("No user found with the given email!")){
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/sendOTP")
    public String sendOTP(HttpServletRequest request, @RequestBody OTP otp){
//        System.out.println("opt = "+otp.getId()+" inside controller");
//
        String sessionId = request.getSession(true).getId();
        Bucket bucket = otpBuckets.computeIfAbsent(sessionId, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofDays(1))))
                        .build()
        );

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (!probe.isConsumed()) {
            return "Too many OTP requests for today. Try again tomorrow.";
        }

        if(emailService.sendMail(otp.getId())==0)return "failure";
        return "success";
    }

}
