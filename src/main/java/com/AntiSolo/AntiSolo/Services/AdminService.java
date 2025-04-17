package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import com.AntiSolo.AntiSolo.Repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    EmailService emailService;
    @Autowired
    ReportRepo reportRepo;
    public void warnUser(String email,String userName,String projectTittle){
        emailService.sendWarningEmail(email,userName,projectTittle);
    }

    public List<ReportEntity> getAllReports(){

        return reportRepo.findAll();
    }
}
