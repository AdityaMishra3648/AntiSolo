package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.Entity.ProjectRequest;
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

    @PostMapping("/save")
    public String saveProject(@RequestBody ProjectRequest projectRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        if(!loggedInUser.equals(projectRequest.getAuthor()))return "Invalid Request";
        return projectService.save(projectRequest);
//        return "working on the API";
    }
}
