package com.AntiSolo.AntiSolo.Services;


import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import com.AntiSolo.AntiSolo.HelperEntities.Member;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.HelperEntities.RatingEntity;
import com.AntiSolo.AntiSolo.HelperEntities.WarningEntity;
import com.AntiSolo.AntiSolo.Repository.ReportRepo;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    UserRepo ur;
    @Autowired
    public OTPService otpService;
    @Autowired
    NotificationService notificationService;

    @Autowired
    ReportRepo reportRepo;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9._]{2,24}$");
    public  static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void saveDirect(User user){
        ur.save(user);
    }


    public String saveEntry(User user,String otp,boolean initial){
//        je.setPassword(passwordEncoder.encode(je.getPassword()));
//        ur.save(je);

        System.out.println("inside UserService");
        try {
            if(!otpService.validateOTP(user.getEmail(),otp))return "Incorrect OTP";
            if (initial && (user.getUserName() == null || user.getUserName().trim().isEmpty()) ) {
                return "Username cannot be empty";
            }
            if(initial && ur.findByEmail(user.getEmail()).isPresent())return "User with this email already exists";
            if(initial && ur.findById(user.getUserName()).isPresent())return "Username already taken";
//            if(user.getUserName().contains())return "Username already taken";
            if (initial && (user.getUserName().length() < 3 || user.getUserName().length() > 25) ) {
                return "Username must be between 3 and 25 characters";
            }
            if (initial && user.getUserName().contains(" ")) {
                return "Username cannot contain spaces";
            }

            if (initial && !USERNAME_PATTERN.matcher(user.getUserName()).matches()) {
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
            user.setRole("USER");


            if(!initial){
                Optional<User> dbUser = ur.findByEmail(user.getEmail());
                if(dbUser.isEmpty())return "No user found with the given email!";
                dbUser.get().setPassword(passwordEncoder.encode(user.getPassword()) );
                ur.save(dbUser.get());
                return dbUser.get().getUserName();
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
        return true;
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

    public void updateRating(String senderUserName,int rating,String targetUserName){
        if(rating>10 || rating<0)return;
        if(!shareAnyProject(senderUserName,targetUserName))return;
        boolean flag = false;
        Optional<User> sender = getById(senderUserName);
        if(sender.isEmpty())return;
        Optional<User> target = getById(targetUserName);
        if(target.isEmpty())return;
        int n = target.get().getRaters().size();
        for(int i=0;i<n;i++){
            RatingEntity r = target.get().getRaters().get(i);
            if(r.getUserName().equals(senderUserName)){
                r.setRating(rating);
                flag = true;
            }
        }
        if(!flag){
            target.get().addRater(new RatingEntity(senderUserName,rating));
            target.get().setTotalRaters(target.get().getTotalRaters()+1);
        }
        double totalRating = 0;
        int m = target.get().getRaters().size();
        for(RatingEntity r:target.get().getRaters())totalRating += (double)r.getRating();
        target.get().setAverageRating(totalRating/(double)m );
        saveDirect(target.get());
    }

    public boolean shareAnyProject(String user1,String user2){
        Optional<User> u1 = getById(user1);
        Optional<User> u2 = getById(user2);
        if(user1.isEmpty() || user2.isEmpty())return false;
        Set<ObjectId> st = new HashSet<>();
        for(ObjectId s:u1.get().getTeams())st.add(s);
        for(ObjectId s:u2.get().getTeams())if(st.contains(s))return true;
        return false;
    }
    public void unfriend(String user1,String user2){
        Optional<User> u1 = getById(user1);
        Optional<User> u2 = getById(user2);
        if(u1.isEmpty() || u2.isEmpty())return;
        u1.get().getBuddies().removeIf(buddy->buddy.getName().equals(u2.get().getUserName()));
        u2.get().getBuddies().removeIf(buddy->buddy.getName().equals(u1.get().getUserName()));
        saveDirect(u1.get());
        saveDirect(u2.get());
    }

    public void editProfile(User edited){
        Optional<User> user = getById(edited.getUserName());
        if(user.isEmpty())return;
        user.get().setBio(edited.getBio());
        user.get().setSkills(edited.getSkills());
        user.get().setInterests(edited.getInterests());
        user.get().setSocialLinks(edited.getSocialLinks());
        saveDirect(user.get());
    }

    public void fileReport(WarningEntity warningEntity){
//        String projectId = warningEntity.getProjectId();

//        Optional<User> p = getById(new ObjectId(projectId));
//        if(p.isEmpty())throw new RuntimeException();;
//        Optional<User> user = userService.getById(p.get().getAuthor());
//        if(user.isEmpty())throw new RuntimeException();;
//        Optional<ReportEntity> temp = reportRepo.findByProjectId(projectId);
//        if(temp.isPresent())throw new RuntimeException();
        Optional<User> guilty = getById(warningEntity.getReported());
        if(guilty.isEmpty())throw new RuntimeException();
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setEmail(guilty.get().getEmail());
        reportEntity.setType(1);
        reportEntity.setUserName(guilty.get().getUserName());
        reportEntity.setCreatedAt(Instant.now());
        reportEntity.setMessage(warningEntity.getMessage());
        reportEntity.setReportFrom(warningEntity.getReportFrom());
        reportRepo.save(reportEntity);
    }



}
