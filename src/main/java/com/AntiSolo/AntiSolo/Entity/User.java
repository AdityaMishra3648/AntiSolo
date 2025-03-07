package com.AntiSolo.AntiSolo.Entity;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
public class User {
    @Id
    private String userName;

    @NonNull
    private String password;


    @NonNull
    private String email;

    @DBRef(lazy = true)
    private List<Project> projects;

    @DBRef(lazy = true)
    private List<Project> applied;

    @DBRef(lazy = true)
    private List<Project> teams;

    public void addTeamProject(Project p){
        teams.add(p);
    }
    public void addAppliedProject(Project p){
        applied.add(p);
    }


    public void addproject(Project p){
        projects.add(p);
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getApplied() {
        return applied;
    }

    public void setApplied(List<Project> applied) {
        this.applied = applied;
    }

    public List<Project> getTeams() {
        return teams;
    }

    public void setTeams(List<Project> teams) {
        this.teams = teams;
    }

    public User(String userName, @NonNull String password, @NonNull String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.projects = new ArrayList<>();
        this.applied = new ArrayList<>();
        this.teams = new ArrayList<>();
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
