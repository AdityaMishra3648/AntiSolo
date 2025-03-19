package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.Notification;
import com.AntiSolo.AntiSolo.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    JwtHelper jwtHelper;

    @GetMapping
    public Page<Notification> getNotifications(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String parentName = jwtHelper.getUsernameFromToken(token);
        return notificationService.getUsersByParentName(parentName, page, size);
    }

    @PostMapping
    public void saveNotification(@RequestHeader("Authorization") String token,@RequestBody Notification notification) {
//        System.out.println("saving notification");
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        String parentName = jwtHelper.getUsernameFromToken(token);

        notification.setParent(parentName);
        notificationService.saveEntry(notification);
    }

}
