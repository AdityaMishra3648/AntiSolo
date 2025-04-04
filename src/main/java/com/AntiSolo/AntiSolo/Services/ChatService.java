package com.AntiSolo.AntiSolo.Services;

import com.AntiSolo.AntiSolo.Entity.ChatMessage;
import com.AntiSolo.AntiSolo.Repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    public boolean areFriends(String sender,String recepient){
        return true;
    }
    public void saveMessage(ChatMessage chatMessage){
//        chatMessage.setCreatedAt(Instant.now());
        chatMessageRepository.save(chatMessage);
        //save message to db
    }
    public List<ChatMessage> getMessages(String chatId, Instant cursorCreatedAt, int pageSize) {
        PageRequest pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (cursorCreatedAt == null) {
            // First page (latest messages)
            return chatMessageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);
        }
        // Fetch older messages (createdAt < cursorCreatedAt)
        return chatMessageRepository.findByChatIdAndCreatedAtBeforeOrderByCreatedAtDesc(chatId, cursorCreatedAt, pageable);
    }
}
