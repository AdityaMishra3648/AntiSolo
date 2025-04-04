package com.AntiSolo.AntiSolo.Services;


import com.AntiSolo.AntiSolo.HelperEntities.Member;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    UserRepo ur;
    @Autowired
    public OTPService otpService;
    @Autowired
    NotificationService notificationService;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9._]{2,24}$");
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
            if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
                return "Username cannot be empty";
            }
            if(ur.findByEmail(user.getEmail()).isPresent())return "User with this email already exists";
            if(ur.findById(user.getUserName()).isPresent())return "Username already taken";
//            if(user.getUserName().contains())return "Username already taken";
            if (user.getUserName().length() < 3 || user.getUserName().length() > 15) {
                return "Username must be between 3 and 15 characters";
            }
            if (user.getUserName().contains(" ")) {
                return "Username cannot contain spaces";
            }

            if (!USERNAME_PATTERN.matcher(user.getUserName()).matches()) {
                return "Username can only contain letters, digits, dot (.) or underscore (_) and must start with a letter";
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return "Password cannot be empty";
            }
            if (user.getPassword().contains(" ")) {
                return "Password cannot contain spaces";
            }
            if (user.getPassword().length()<4 || user.getPassword().length()>20) {
                return "Password length must be between 4 to 20 letters";
            }

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

    public List<User> findUsersWithPrefix(String username){
        return ur.findByUserNamePrefix(username);
    }
}
