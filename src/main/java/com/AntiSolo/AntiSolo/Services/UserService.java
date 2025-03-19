package com.AntiSolo.AntiSolo.Services;


import com.AntiSolo.AntiSolo.Entity.Member;
import com.AntiSolo.AntiSolo.Entity.Notification;
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
    @Autowired
    NotificationService notificationService;

    public  static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void saveDirect(User user){
        ur.save(user);
    }
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
    public String checkUser(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<User> curr = getById(user.getUserName());
        if(!curr.isPresent())return "Incorrect UserName!";
        String stored = curr.get().getPassword();
        String camePassword = user.getPassword();
        if(!encoder.matches(camePassword,stored))return "Incorrect Password!";
        return "Login Success";
    }
    public Optional<User> getByEmail(String email){
        return ur.findByEmail(email);
    }

    public boolean sendFriendRequest(User sender,User user){
        for(Member friend:user.getBuddies()){
            if(friend.getName().equals(sender.getUserName()) )return false;
        }
       for(Member friend:sender.getBuddies()){
            if(friend.getName().equals(user.getUserName()) )return false;
       }

       boolean widrawFriendRequest = user.getFriendRequest().removeIf(request->request.getName().equals(sender.getUserName()));
       if(widrawFriendRequest){
           saveDirect(user);
           return true;
       }
       boolean RequesterAlreadyHasRequest = false;
        for(Member request:sender.getFriendRequest()){
            if(request.getName().equals(user.getUserName())){
                RequesterAlreadyHasRequest = true;
            }
        }
        if(RequesterAlreadyHasRequest){
            sender.getFriendRequest().removeIf(request->request.getName().equals(user.getUserName()));
            sender.addBuddies(new Member(user.getUserName(),user.getProfileImageUrl()));
            user.addBuddies(new Member(sender.getUserName(),sender.getProfileImageUrl()));
            saveDirect(sender);
            saveDirect(user);
            notificationService.AcceptedRequestNotification(sender.getUserName(),user.getUserName());
            return false;
        }
        user.addFriendRequest(new Member(sender.getUserName(),sender.getProfileImageUrl()));
        saveDirect(user);
        notificationService.friendRequestReceivedNotification(sender.getUserName(),user.getUserName());
        return true;
    }

    public boolean acceptFriendRequest(User sender,User user){

        for(Member friend:user.getBuddies()){
            if(friend.getName().equals(sender.getUserName()) )return false;
        }
        for(Member friend:sender.getBuddies()){
            if(friend.getName().equals(user.getUserName()) )return false;
        }
        boolean userSentRequestToSender = sender.getFriendRequest().removeIf(request->request.getName().equals(user.getUserName()));
//        System.out.println("seder has request from user ? = "+userSentRequestToSender+" sender = "+sender.getUserName()+" user = "+user.getUserName());
        if(!userSentRequestToSender)return false;
//        sender.getFriendRequest().removeIf(request->request.getName().equals(user.getUserName()));
        sender.addBuddies(new Member(user.getUserName(),user.getProfileImageUrl()));
        user.addBuddies(new Member(sender.getUserName(),sender.getProfileImageUrl()));
        saveDirect(sender);
        saveDirect(user);
        notificationService.AcceptedRequestNotification(sender.getUserName(),user.getUserName());
        return true;
    }

}
