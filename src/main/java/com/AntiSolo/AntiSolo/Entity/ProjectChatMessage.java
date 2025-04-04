package com.AntiSolo.AntiSolo.Entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "GroupMessages")
public class ProjectChatMessage {
    private ObjectId id;
    private String projectId;
    private String sender;
    private String message;


    @CreatedDate
    private Instant createdAt;
    public ProjectChatMessage() {
        this.createdAt = Instant.now();
    }

    public ProjectChatMessage(ObjectId id, String projectId, String sender, String message, Instant createdAt) {
        this.id = id;
        this.projectId = projectId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
