package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.Entity.ProjectChatMessage;
import com.AntiSolo.AntiSolo.Repository.GroupChatsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class GroupChatService {
    @Autowired
    private GroupChatsRepo groupChatsRepo;

    public void saveMessage(ProjectChatMessage projectChatMessage){
//        chatMessage.setCreatedAt(Instant.now());
        groupChatsRepo.save(projectChatMessage);
        //save message to db
    }

    public List<ProjectChatMessage> getMessages(String projectId, Instant cursorCreatedAt, int pageSize) {
        PageRequest pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (cursorCreatedAt == null) {
            // First page (latest messages)
            return groupChatsRepo.findByProjectIdOrderByCreatedAtDesc(projectId, pageable);
        }
        // Fetch older messages (createdAt < cursorCreatedAt)
        return groupChatsRepo.findByProjectIdAndCreatedAtBeforeOrderByCreatedAtDesc(projectId, cursorCreatedAt, pageable);
    }

}
