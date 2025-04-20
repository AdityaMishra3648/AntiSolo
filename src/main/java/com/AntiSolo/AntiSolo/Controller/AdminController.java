package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Entity.Project;
import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.HelperEntities.Member;
import com.AntiSolo.AntiSolo.HelperEntities.WarningEntity;
import com.AntiSolo.AntiSolo.Repository.ReportRepo;
import com.AntiSolo.AntiSolo.Repository.UserRepo;
import com.AntiSolo.AntiSolo.Services.AdminService;
import com.AntiSolo.AntiSolo.Services.ProjectService;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;

    @Autowired
    ReportRepo reportRepo;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserRepo ur;


    @GetMapping("/hii")
    public String hii(){
        return "hey";
    }


    @PostMapping("/warnUser")
    public void warnUser(@RequestBody ReportEntity reportEntity){
//        Optional<User> user = userService.getById(warningEntity.getUserName());
//        if(user.isEmpty())return;
        if(reportEntity.getType()==0){
            adminService.warnUser(reportEntity.getEmail(), reportEntity.getUserName(),reportEntity.getProjectId());
        }else if(reportEntity.getType()==1){
            adminService.warnUserForProfileReport(reportEntity.getEmail(), reportEntity.getUserName());
        }
    }

    @PostMapping("/deleteProject")
    public void deleteProject(@RequestBody ReportEntity reportEntity){
        projectService.deleteProject(reportEntity.getProjectId());
    }
    @PostMapping("/deleteUser")
    public void  deleterUser(@RequestBody ReportEntity reportEntity){
        userService.deleteByid(reportEntity.getUserName());
        String id = reportEntity.getUserName();
        Optional<User> user =  ur.findById(id);
        if(user.isEmpty())return;
        for(ObjectId projectId:user.get().getProjects()){
            String pid = projectId.toString();
            projectService.deleteProject(pid);
        }

        for(Member member:user.get().getBuddies()){
            userService.unfriend(user.get().getUserName(),member.getName());
        }

        for(ObjectId pid:user.get().getTeams()){
            Optional<Project> project = projectService.getProjectById(pid);
            if(project.isEmpty())continue;
            project.get().getMembers().removeIf(member->member.getName().equals(user.get().getUserName()));
            projectService.saveDirect(project.get());
        }

        for(ObjectId pid:user.get().getApplied()){
            Optional<Project> project = projectService.getProjectById(pid);
            if(!project.isPresent())continue;
            project.get().getApplicants().removeIf(applicant->applicant.getName().equals(id));
            projectService.saveDirect(project.get());
        }

        List<User> sentFriendRequests = ur.findAllByFriendRequestName(id);
        for(User u:sentFriendRequests){
            u.getFriendRequest().removeIf(request->request.getName().equals(id));
            userService.saveDirect(u);
        }

        if (ur.existsById(id)) {
            ur.deleteById(id);
            return;  // Indicates deletion was successful
        } else {
            return; // Indicates the ID was not found
        }
    }

    @GetMapping("/getAllReports")
    public List<ReportEntity> getALlReports(){
        System.out.println("ajnsdk");
        return adminService.getAllReports();
    }

    @GetMapping("/reports")
    public Page<ReportEntity> getReportsByType(
            @RequestParam int type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        System.out.println("type = "+type+" page = "+page+" size = "+size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reportRepo.findByTypeOrderByCreatedAtDesc(type, pageable);
    }


}
