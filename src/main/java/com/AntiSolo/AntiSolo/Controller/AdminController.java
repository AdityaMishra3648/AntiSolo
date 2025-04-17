package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import com.AntiSolo.AntiSolo.Entity.User;
import com.AntiSolo.AntiSolo.HelperEntities.WarningEntity;
import com.AntiSolo.AntiSolo.Repository.ReportRepo;
import com.AntiSolo.AntiSolo.Services.AdminService;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/hii")
    public String hii(){
        return "hey";
    }


    @PostMapping("/warnUser")
    public void warnUser(@RequestBody ReportEntity reportEntity){
//        Optional<User> user = userService.getById(warningEntity.getUserName());
//        if(user.isEmpty())return;
        adminService.warnUser(reportEntity.getEmail(), reportEntity.getUserName(),reportEntity.getProjectId());
    }

    @GetMapping("/getAllReports")
    public List<ReportEntity> getALlReports(){
        System.out.println("ajnsdk");
        return adminService.getAllReports();
    }
}
