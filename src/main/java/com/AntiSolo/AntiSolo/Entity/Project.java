package com.AntiSolo.AntiSolo.Entity;

import com.AntiSolo.AntiSolo.Configuration.ObjectIdDeserializer;
import com.AntiSolo.AntiSolo.Configuration.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;



@Getter
@Setter
@Document(collection = "project")
public class Project {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class) // Convert ObjectId -> String
    @JsonDeserialize(using = ObjectIdDeserializer.class) // Convert String -> ObjectId
    private ObjectId id;


    @NonNull
    private int teamSize;
    @NonNull
    private int filled;

    private String domain;
    private Set<String> tags;

    private String author;
    private String authorImage;

    private String image;
    private String title;
    private String description;
    List<String> technologies;
    private Date createdAt;
    private String status;

    private List<Member> applicants;


    private List<Member> members;

    public void addMember(Member applicant){
        members.add(applicant);
    }

    public void addApplicant(Member applicant){
        applicants.add(applicant);
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setAuthorImage(String authorImage){
        this.authorImage = authorImage;
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
    public Project() {
    }

    @PersistenceConstructor
    public Project(ObjectId id, int teamSize, int filled, String domain, Set<String> tags, String author, String authorImage, String image, String title, String description, List<String> technologies, Date createdAt, String status, List<Member> applicants, List<Member> members) {
        this.id = id;
        this.teamSize = teamSize;
        this.filled = filled;
        this.domain = domain;
        this.tags = tags;
        this.author = author;
        this.authorImage = authorImage;
        this.image = image;
        this.title = title;
        this.description = description;
        this.technologies = technologies;
        this.createdAt = createdAt;
        this.status = status;
        this.applicants = applicants;
        this.members = members;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
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

    public List<Member> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Member> applicants) {
        this.applicants = applicants;
    }



    public Project(String author, int teamSize, int filled,String domain, HashSet<String> tags,String image,String title,
                   String description ,List<String> technologies, List<Member> members) {
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
        this.createdAt = new Date();
        this.status = "Recruiting";
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
