package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Entity.ChatMessage;
import com.AntiSolo.AntiSolo.Services.ChatService;
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

        return chatService.getMessages(chatId, cursorCreatedAt, pageSize);
    }
}
