package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.Entity.ProjectRequest;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.Repository.ProjectRepo;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    public ProjectRepo projectRepo;

    @Autowired
    public UserService userService;

    @Autowired
    UserRepo ur;

    public String save(ProjectRequest projectRequest){
        Optional<User> user = userService.getById(projectRequest.getAuthor());
        if(!user.isPresent())return "Invalid Request!";
        if(projectRequest.getTeamSize()<=1)return "Team size can not be 0 or 1";
        if(projectRequest.getDomain()==null || projectRequest.getDomain().isEmpty())return "Select Valid Domain!";
        Project p = new Project(user.get(),projectRequest.getTeamSize(),
                    projectRequest.getFilled(),projectRequest.getDomain());
        projectRepo.save(p);
        user.get().addproject(p);
        ur.save(user.get());
                        //after saving the user also reload the entire page so that it again fetches the
        //user details and the array with this project added is also added to list
        return "Saved successfully";
    }
}
