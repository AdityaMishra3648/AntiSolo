package com.AntiSolo.AntiSolo.Controller;

import com.AntiSolo.AntiSolo.Configuration.JwtHelper;
import com.AntiSolo.AntiSolo.Entity.ChatMessage;
import com.AntiSolo.AntiSolo.Entity.ProjectChatMessage;
import com.AntiSolo.AntiSolo.Services.ChatService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Controller
//@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final JwtHelper jwtHelper;
    private final ChatService chatService;  // Service for database operations (saving messages, checking friends)
    ChatController(SimpMessagingTemplate messagingTemplate, JwtHelper jwtHelper, ChatService chatService){
        this.chatService = chatService;
        this.jwtHelper = jwtHelper;
        this.messagingTemplate = messagingTemplate;
    }
    /**
     * Handles private messages sent from one user to another.
     */


    public String generateChatId(String user1, String user2) {
        String[] users = {user1, user2};
        Arrays.sort(users); // Ensures consistent ordering
        return users[0] + "$" + users[1]; // Concatenates with '$' separator
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Header("Authorization") String token, ChatMessage message) {
        // Extract sender's username from JWT
//        System.out.println("send private message called");
        String senderUsername = jwtHelper.getUsernameFromToken(token.replace("Bearer ", ""));

        System.out.println("send private message called");
        // Validate if the sender matches the token
        if (!senderUsername.equals(message.getSender())) {
            throw new RuntimeException("Unauthorized sender!");
        }

        // Check if sender and recipient are friends
        if (!chatService.areFriends(message.getSender(), message.getRecipient())) {
            throw new RuntimeException("You can only message friends!");
        }
        String chatId = generateChatId(message.getRecipient(),message.getSender());
        message.setChatId(chatId);

        // Save the message to DB
        chatService.saveMessage(message);
        // Deliver message to recipient's WebSocket subscription
        messagingTemplate.convertAndSendToUser(chatId, "/queue/private", message);
    }


    @MessageMapping("/group-message")
    public void sendGroupMessage(@Header("Authorization") String token, ProjectChatMessage message) {
        // Extract sender's username from JWT
//        System.out.println("send private message called");
        String senderUsername = jwtHelper.getUsernameFromToken(token.replace("Bearer ", ""));

        System.out.println("send Group message called");
        // Validate if the sender matches the token
        if (!senderUsername.equals(message.getSender())) {
            throw new RuntimeException("Unauthorized sender!");
        }
//        chatService.saveMessage(message);
        // Deliver message to recipient's WebSocket subscription
//        messagingTemplate.convertAndSendToUser(chatId, "/queue/private", message);
        System.out.println("sending message to "+message.getProjectId());
        messagingTemplate.convertAndSend("/group/" + message.getProjectId(), message);
        System.out.println("sent message to "+message.getProjectId());

    }



}
