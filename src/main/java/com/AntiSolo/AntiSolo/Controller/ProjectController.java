package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.Entity.ProjectRequest;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@CrossOrigin
public class ProjectController {
    @Autowired
    public ProjectService projectService;


    @Autowired
    public JwtHelper jwtHelper;


    @PostMapping("/test")
    public String saveProject(@RequestBody User user, @RequestHeader("Authorization") String token) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("came with user = "+user.getUserName());
        return "hii";
    }
    @PostMapping("/save")
    public String saveProject(@RequestBody ProjectRequest projectRequest,@RequestHeader("Authorization") String token){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInUser = authentication.getName();
//        projectRequest.setAuthor(jwtHelper.getUsernameFromToken(projectRequest.getAuthor()));
//        if(!loggedInUser.equals(projectRequest.getAuthor()))return "Invalid Request";
        // Remove "Bearer " prefix if present
        System.out.println("arrived in post");
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        projectRequest.setFilled(1);
        String username = jwtHelper.getUsernameFromToken(token);
        projectRequest.setAuthor(username);
        System.out.println("sending for project service");
        return projectService.save(projectRequest);
//        return "working on the API";
    }
}
