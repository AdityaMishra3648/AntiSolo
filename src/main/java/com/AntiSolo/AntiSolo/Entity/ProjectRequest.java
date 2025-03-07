package com.AntiSolo.AntiSolo.Entity;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    @NonNull
    public int teamSize;
    @NonNull
    public int filled;

    public String domain;
    public List<String> tags;
    public String author;

    public String image;
    public String title;
    public String description;
    public List<String> technologies;
    public Date createdAt;
    public String status;
    public List<String> applicants;
    public List<String> members;

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

    public List<String> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<String> applicants) {
        this.applicants = applicants;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public ProjectRequest(int teamSize, int filled, String domain,List<String> tags,String image,String title,String description,
                          List<String> technologies,List<String> applicants,List<String> members) {
        this.teamSize = teamSize;
        this.filled = filled;
        this.domain = domain;
        this.tags = tags;
        this.image = image;
        this.title = title;
        this.description = description;
        this.technologies = technologies;
        this.applicants = applicants;
        this.members = members;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
