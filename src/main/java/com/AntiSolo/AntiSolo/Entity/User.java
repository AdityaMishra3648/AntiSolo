package com.AntiSolo.AntiSolo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.swing.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String userName;

    @NonNull
    private String password;


    @NonNull
    private String email;

    private List<ObjectId> projects;

    private List<ObjectId> applied;

    private List<ObjectId> teams;

    @Setter
    @Getter
    private String profileImageUrl;
    private String bio;
    private Date accountCreationDate;

    private List<Member> buddies;
    List<Member> friendRequest;


    private double averageRating;
    private int totalRaters;
    private Map<String, String> socialLinks;  // Social profiles (GitHub, LinkedIn, etc.)
    private List<String> notifications;
    private List<String> skills;  // List of skills (Java, Python, React, etc.)
    private List<String> interests;  // Interests in project domains
    public String getProfileImageUrl(){
        return this.profileImageUrl;
    }

    public void addTeamProject(ObjectId p){
        teams.add(p);
    }

    public void addNotification(String message) {
        notifications.add(message);
    }

    public void addSkill(String skill) {
        skills.add(skill);
    }
    public void addBuddies(Member buddy){
        this.buddies.add(buddy);
    }
    public void addFriendRequest(Member req){
        this.friendRequest.add(req);
    }

    public void addInterest(String interest) {
        interests.add(interest);
    }

    public void addSocialLink(String platform, String link) {
        socialLinks.put(platform, link);
    }


    public void addAppliedProject(ObjectId p){
        applied.add(p);
    }


    public void addproject(ObjectId p){
        projects.add(p);
    }

    public List<ObjectId> getProjects() {
        return projects;
    }

    public List<Member> getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(List<Member> friendRequest) {
        this.friendRequest = friendRequest;
    }

    public void setProjects(List<ObjectId> projects) {
        this.projects = projects;
    }

    public List<ObjectId> getApplied() {
        return applied;
    }

    public void setApplied(List<ObjectId> applied) {
        this.applied = applied;
    }

    public void setTeams(List<ObjectId> teams) {
        this.teams = teams;
    }
    public List<ObjectId> getTeams() {
        return this.teams;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public List<Member> getBuddies() {
        return buddies;
    }

    public void setBuddies(List<Member> buddies) {
        this.buddies = buddies;
    }

    public int getTotalRaters() {
        return totalRaters;
    }

    public void setTotalRaters(int totalRaters) {
        this.totalRaters = totalRaters;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Map<String, String> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(Map<String, String> socialLinks) {
        this.socialLinks = socialLinks;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public User(String userName, @NonNull String password, @NonNull String email) {
        String[] images = new String[]{"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1GvJLSBZLa0E0rxh1prAXaV9zq5mnU7c6yv18NNkJSVY3BcGOhBJDSal5kKwW3nzZI88&usqp=CAU",
        "https://www.redditstatic.com/avatars/avatar_default_02_C18D42.png",
        "https://www.redditstatic.com/avatars/avatar_default_02_7E53C1.png",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcauUs8R0kv2QxC66jCMgDWM1P4OVSDK7KsQnsDPGewdmNzgN0huSYTCHzhqkAGNPxV3o&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsFoHGp5Yk3O13acR_yhwaT91sZQb_ayxfwf4SzHZ_YqKTIWe9Oh-gN-2XEo-CoWJhCko&usqp=CAU",
        "https://www.redditstatic.com/avatars/avatar_default_01_A5A4A4.png"};
        Random random = new Random();
        int randomNumber = random.nextInt(6);
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.projects = new ArrayList<>();
        this.applied = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.profileImageUrl = images[randomNumber];
        this.accountCreationDate = new Date();
        this.bio = "";
        this.buddies = new ArrayList<>();
        this.averageRating = 0.0;
        this.totalRaters = 0;
        this.skills = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.socialLinks = new HashMap<>();
        this.notifications = new ArrayList<>();
        this.friendRequest = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }


}
