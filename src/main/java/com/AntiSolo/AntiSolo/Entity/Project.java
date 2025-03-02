package com.AntiSolo.AntiSolo.Entity;

import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Document(collection = "project")
public class Project {

    @Id
    private ObjectId id;
    @NonNull
    private int teamSize;
    @NonNull
    private int filled;

    private String domain;
    private HashSet<String> tags;
    @DBRef
    private User author;

    public Project(User author, int teamSize, int filled,String domain) {
        this.author = author;
        this.teamSize = teamSize;
        this.filled = filled;
        this.domain = domain;
        this.tags = new HashSet<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getFilled() {
        return filled;
    }

    public void setFilled(int filled) {
        this.filled = filled;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
