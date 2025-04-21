package com.AntiSolo.AntiSolo.Entity;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "Messages")
//@Getter
//@Setter
public class ChatMessage {
    @Id
    ObjectId id;

    String chatId;

    @NonNull
    String sender;
    @NonNull
    String recipient;
    @NonNull
    String message;

    @CreatedDate
    private Instant createdAt;
    public ChatMessage() {
        this.createdAt = Instant.now();
    }

    public ChatMessage(String chatId, @NonNull String sender, @NonNull String recipient, @NonNull String message) {
        this.chatId = chatId;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.createdAt = Instant.now();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @NonNull
    public String getSender() {
        return sender;
    }

    public void setSender(@NonNull String sender) {
        this.sender = sender;
    }

    @NonNull
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(@NonNull String recipient) {
        this.recipient = recipient;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }
}
