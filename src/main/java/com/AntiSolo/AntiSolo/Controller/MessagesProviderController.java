package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Entity.ChatMessage;
import com.AntiSolo.AntiSolo.Entity.ProjectChatMessage;
import com.AntiSolo.AntiSolo.Services.ChatService;
import com.AntiSolo.AntiSolo.Services.GroupChatService;
import com.AntiSolo.AntiSolo.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessagesProviderController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private GroupChatService groupChatService;

    public String generateChatId(String user1, String user2) {
        String[] users = {user1, user2};
        Arrays.sort(users); // Ensures consistent ordering
        return users[0] + "$" + users[1]; // Concatenates with '$' separator
    }


    @GetMapping("/{chatId}")
    public List<ChatMessage> getMessages(
            @PathVariable String chatId,
            @RequestParam(required = false) Instant cursorCreatedAt,
            @RequestParam(defaultValue = "10") int pageSize) {
        //add here logic that the token that came here is of the user that is a friend of the current user only fetch the previous message
        //not adding right now cause lack of time
        return chatService.getMessages(chatId, cursorCreatedAt, pageSize);
    }

    @GetMapping("/group/{projectId}")
    public List<ProjectChatMessage> getGroupMessage(
            @PathVariable String projectId,
            @RequestParam(required = false) Instant cursorCreatedAt,
            @RequestParam(defaultValue = "10") int pageSize) {
        //add here logic that the token that came here is of the user that is a part of the projectthen only fetch the previous message
        //not adding right now cause lack of time
        return groupChatService.getMessages(projectId, cursorCreatedAt, pageSize);
    }
}
