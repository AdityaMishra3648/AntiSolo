package com.AntiSolo.AntiSolo.Controller;


import com.AntiSolo.AntiSolo.Entity.OTP;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Services.EmailService;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signUp")
@CrossOrigin
public class SignUpController {
    @Autowired
    public EmailService emailService;

    @Autowired
    public UserService userService;

    @PostMapping("/saveUser/{otp}")
    public String saveUser(@PathVariable String otp, @RequestBody User user){
        System.out.println("opt = "+otp+" user = "+user+" inside controller "+otp);
        return userService.saveEntry(user,otp);
    }

    @PostMapping("/sendOTP")
    public String sendOTP(@RequestBody OTP otp){
        System.out.println("opt = "+otp.getId()+" inside controller");
        if(emailService.sendMail(otp.getId())==0)return "failure";
        return "success";
    }

}
