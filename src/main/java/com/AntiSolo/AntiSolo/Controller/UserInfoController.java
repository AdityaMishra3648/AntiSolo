package com.AntiSolo.AntiSolo.Controller;


import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.Member;
import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Services.CloudinaryImageService;
import com.AntiSolo.AntiSolo.Services.ProjectService;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.bson.types.ObjectId;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
