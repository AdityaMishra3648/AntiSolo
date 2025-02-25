package com.AntiSolo.AntiSolo.Services;


import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo ur;
    @Autowired
    public OTPService otpService;

    public  static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public String saveEntry(User user,String otp){
//        je.setPassword(passwordEncoder.encode(je.getPassword()));
//        ur.save(je);
        System.out.println("inside UserService");
        try {
            if(!otpService.validateOTP(user.getEmail(),otp))return "Incorrect OTP";
            if(ur.findByEmail(user.getEmail()).isPresent())return "User with this email already exists";
            if(ur.findById(user.getUserName()).isPresent())return "Username already taken";
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = ur.save(user);

            if(savedUser != null && savedUser.getUserName() != null)return "saved Successfully";
            return "Error occured while saving user";// ✅ Success if ID is assigned
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage()); // Log the error
            return "Error occured while saving user";
        }
    }

    public Optional<User> getById(String id){
        return ur.findById(id);
    }
    public boolean deleteByid(String id){
        if (ur.existsById(id)) {
            ur.deleteById(id);
            return true;  // Indicates deletion was successful
        } else {
            return false; // Indicates the ID was not found
        }
    }

    public Optional<User> getByEmail(String email){
        return ur.findByEmail(email);
    }
}
