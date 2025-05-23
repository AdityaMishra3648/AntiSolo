package com.AntiSolo.AntiSolo.Controller;


import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.HelperEntities.Member;
import com.AntiSolo.AntiSolo.HelperEntities.RatingEntity;
import com.AntiSolo.AntiSolo.HelperEntities.WarningEntity;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import com.AntiSolo.AntiSolo.Services.CloudinaryImageService;
import com.AntiSolo.AntiSolo.Services.ProjectService;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/User")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CloudinaryImageService cloudinaryImageService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/saveImage")
    public ResponseEntity<Map> saveImage(@RequestParam("image")MultipartFile file,@RequestHeader("Authorization") String token){
        Map data = this.cloudinaryImageService.upload(file);
        System.out.println(data.get("url"));
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtHelper.getUsernameFromToken(token);
        Optional<User> user = userService.getById(username);
        if(!user.isPresent())return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        user.get().setProfileImageUrl(data.get("url").toString());
        userService.saveDirect(user.get());
        String imgUrl = data.get("url").toString();
        for(ObjectId id:user.get().getProjects()){
            Optional<Project> project = projectService.getProjectById(id);
            if(project.isEmpty())continue;
            project.get().setAuthorImage(imgUrl);
            projectService.saveDirect(project.get());
        }
        for(ObjectId id:user.get().getTeams()){
            Optional<Project> project = projectService.getProjectById(id);
            if(project.isEmpty())continue;
            for(int i=0;i<project.get().getMembers().size();i++){
                if(project.get().getMembers().get(i).getName().equals(user.get().getUserName()) ){
                    project.get().getMembers().get(i).setImageUrl(imgUrl);
                    break;
                }
            }
            projectService.saveDirect(project.get());
        }
        for(ObjectId id:user.get().getApplied()){
            Optional<Project> project = projectService.getProjectById(id);
            if(!project.isPresent())continue;
            for(int i=0;i<project.get().getApplicants().size();i++){
                if(project.get().getApplicants().get(i).getName().equals(user.get().getUserName()) ){
                    project.get().getApplicants().get(i).setImageUrl(imgUrl);
                    break;
                }
            }
            projectService.saveDirect(project.get());
        }
        for(Member buddy:user.get().getBuddies()){
            Optional<User> buddyUser = userService.getById(buddy.getName());
            if(!buddyUser.isPresent())continue;
            for(int i=0;i<buddyUser.get().getBuddies().size();i++){
                if(buddyUser.get().getBuddies().get(i).getName().equals(user.get().getUserName()) ){
                    buddyUser.get().getBuddies().get(i).setImageUrl(imgUrl);
                    break;
                }
            }
            userService.saveDirect(buddyUser.get());
        }

        List<User> sentFriendRequests = userRepo.findAllByFriendRequestName(username);
        for(User u:sentFriendRequests){
            for(int i=0;i<u.getFriendRequest().size();i++){
                if(u.getFriendRequest().get(i).getName().equals(username)){
                    u.getFriendRequest().get(i).setImageUrl(imgUrl);
                    break;
                }
            }
            userService.saveDirect(u);
        }

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    @GetMapping("/userInfo")
    public ResponseEntity<User> userInfo(@RequestHeader("Authorization") String token){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtHelper.getUsernameFromToken(token);
        Optional<User> user = userService.getById(username);
//        System.out.println("user = "+user.get().getTeams().get(0));
        if(user.isPresent())return new ResponseEntity<>(user.get(),HttpStatus.OK);
        else return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sendFriendRequest/{username}")
    public ResponseEntity<Boolean> sendFriendRequest(@RequestHeader("Authorization") String token,@PathVariable String username){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String senderName = jwtHelper.getUsernameFromToken(token);
        Optional<User> sender = userService.getById(senderName);
        if(sender.isEmpty() || username.equals(senderName))return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        Optional<User> user = userService.getById(username);
        if(user.isEmpty())return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        boolean res = userService.sendFriendRequest(sender.get(),user.get());
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @PostMapping("/confirmRequest/{username}")
    public ResponseEntity<Boolean> acceptFriendRequest(@RequestHeader("Authorization") String token,@PathVariable String username){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String senderName = jwtHelper.getUsernameFromToken(token);
        Optional<User> sender = userService.getById(senderName);
        if(sender.isEmpty() || username.equals(senderName))return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        Optional<User> user = userService.getById(username);
        if(user.isEmpty())return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        boolean res = userService.acceptFriendRequest(sender.get(),user.get());
        return new ResponseEntity<>(res,HttpStatus.OK);
    }


    @PostMapping("/rate")
    public void rate(@RequestHeader("Authorization") String token, @RequestBody RatingEntity ratingEntity){
        //in this we have sent target username in ratingEntity I know its not correct way because it was place for the user who is
        //rating and not for whoom the rating is being updated but I just didn't feel like making new helper entity so make it work
        System.out.println("rate called with toek = "+token);
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String senderName = jwtHelper.getUsernameFromToken(token);
//        Optional<User> sender = userService.getById(senderName);
        if(ratingEntity.getUserName().equals(senderName))return;
        userService.updateRating(senderName,ratingEntity.getRating(),ratingEntity.getUserName());
    }
    @PostMapping("/removeFriend/{friendUserName}")
    public void removeFriend(@RequestHeader("Authorization") String token,@PathVariable String friendUserName){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String senderName = jwtHelper.getUsernameFromToken(token);
        userService.unfriend(senderName,friendUserName);
    }

    @PutMapping("/editUser")
    public void editUser(@RequestHeader("Authorization") String token,@RequestBody User user){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtHelper.getUsernameFromToken(token);
        if(!user.getUserName().equals(username))return;
        userService.editProfile(user);
    }

    @GetMapping("/doTheyShareProject/{user1}")
    public ResponseEntity<Boolean> commonProjectChecker(@RequestHeader("Authorization") String token,@PathVariable String user1){
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String user2 = jwtHelper.getUsernameFromToken(token);
        boolean res = userService.shareAnyProject(user1,user2);
//        System.out.println("checking two users user1 = "+user1+" user2 = "+user2+" gave res = "+res);
        if(res)return new ResponseEntity<>(true,HttpStatus.OK);
        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/reportUser")
    public void ReportProject(@RequestBody WarningEntity warningEntity){
        userService.fileReport(warningEntity);
    }

}
