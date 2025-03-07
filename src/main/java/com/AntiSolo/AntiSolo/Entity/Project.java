package com.AntiSolo.AntiSolo.Entity;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "project")
public class Project {

    @Id
    private ObjectId id;


    @NonNull
    private int teamSize;
    @NonNull
    private int filled;

    private String domain;
    private Set<String> tags;
    @DBRef(lazy = true)
    private User author;

    private String image;
    private String title;
    private String description;
    List<String> technologies;
    private Date createdAt;
    private String status;

    @DBRef(lazy = true)
    private List<User> applicants;

    @DBRef(lazy = true)
    private List<User> members;

    public List<User> getMembers() {
        return members;
    }

    public void setMemebers(List<User> memebers) {
        this.members = memebers;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<User> applicants) {
        this.applicants = applicants;
    }



    public Project(User author, int teamSize, int filled,String domain, HashSet<String> tags,String image,String title,
                   String description ,List<String> technologies, List<User> members) {
        this.author = author;
        this.teamSize = teamSize;
        this.filled = filled;
        this.domain = domain;
        this.tags = tags;
        this.image = image;
        this.title = title;
        this.description = description;
        this.technologies = technologies;
        this.applicants = new ArrayList<>();
        this.members = members;
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

    public Set<String> getTags() {
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
