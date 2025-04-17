package com.AntiSolo.AntiSolo.Entity;

import com.AntiSolo.AntiSolo.Configuration.ObjectIdDeserializer;
import com.AntiSolo.AntiSolo.Configuration.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notification")
public class Notification {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class) // Convert ObjectId -> String
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    String message;
    private String parent;
    private int type;
    private String userName;
    private ObjectId projectId;
    @CreatedDate
    private Instant createdAt;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Notification(int type, String message, String parent, String userName, ObjectId projectId) {
        this.type = type;
        this.parent = parent;
        this.message = message;
        this.userName = userName;
        this.projectId = projectId;
    }
    public Notification(int type,String message,String parent,ObjectId projectId) {
        this.type = type;
        this.message = message;
        this.parent = parent;
        this.projectId = projectId;
    }
    public Notification() {
        // Default constructor needed for deserialization
    }
    public Notification(int type,String message,String parent,String userName) {
        this.type = type;
        this.message = message;
        this.parent = parent;
        this.userName = userName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Notification(int type) {
        this.type = type;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public Notification(ObjectId id,String message,String parent,int type, String userName, ObjectId projectId) {
        this.id = id;
        this.parent = parent;
        this.type = type;
        this.message = message;
        this.userName = userName;
        this.projectId = projectId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

