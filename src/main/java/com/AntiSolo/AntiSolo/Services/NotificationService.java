package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.Entity.Notification;
import com.AntiSolo.AntiSolo.Repository.NotificationRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public Page<Notification> getUsersByParentName(String parent, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepository.findByParent(parent, pageRequest);
    }

    public void saveEntry(Notification notification){
        notificationRepository.save(notification);
    }

    public void friendRequestReceivedNotification(String sender,String receiver){
        Notification notification = new Notification(1,"You have received a new friend request from "+sender,receiver,sender,null);
        saveEntry(notification);
    }
    public void AcceptedRequestNotification(String hasReceived,String whooseReceived){
        Notification notification = new Notification(1,hasReceived+" has accepted your friend request. Start collaborating now!",whooseReceived,hasReceived,null);
        saveEntry(notification);
    }

    public void newApplicationforProjectNotification(String applier, String owner, String projectTitle, ObjectId prId){
        Notification notification = new Notification(2,
                applier+" is interested in joining your project "+projectTitle+". Check out their profile!" ,
                owner,applier,prId);
        saveEntry(notification);
    }
    public void AcceptApplicationforProjectNotification(String applier, String owner, String projectTitle, ObjectId prId){
        Notification notification = new Notification(2,
                "Congratulations! You have been accepted to join "+projectTitle+". Get started now!" ,
                applier,owner,prId);
        saveEntry(notification);
    }
}
