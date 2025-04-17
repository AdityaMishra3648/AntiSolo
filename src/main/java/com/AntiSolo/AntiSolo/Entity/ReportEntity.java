package com.AntiSolo.AntiSolo.Entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("Reports")
public class ReportEntity {
     @Id
     ObjectId id;
     int type;
     private String userName;
     private String projectId;
     private String email;
     @CreatedDate
     private Instant createdAt;
     private String message;
     private String reportFrom;

     public ReportEntity(ObjectId id, String reportFrom, String message, Instant createdAt, String email, String projectId, String userName, int type) {
          this.id = id;
          this.reportFrom = reportFrom;
          this.message = message;
          this.createdAt = createdAt;
          this.email = email;
          this.projectId = projectId;
          this.userName = userName;
          this.type = type;
     }

     public String getReportFrom() {
          return reportFrom;
     }

     public void setReportFrom(String reportFrom) {
          this.reportFrom = reportFrom;
     }

     public ReportEntity(ObjectId id, int type, String userName, String projectId, String email, Instant createdAt, String message) {
          this.id = id;
          this.type = type;
          this.userName = userName;
          this.projectId = projectId;
          this.email = email;
          this.createdAt = createdAt;
          this.message = message;
     }

     public String getMessage() {
          return message;
     }

     public void setMessage(String message) {
          this.message = message;
     }

     public ReportEntity(ObjectId id, int type, String userName, String projectId, String email) {
          this.id = id;
          this.type = type;
          this.userName = userName;
          this.projectId = projectId;
          this.email = email;
     }

     public Instant getCreatedAt() {
          return createdAt;
     }

     public ReportEntity(ObjectId id, int type, String userName, String projectId, String email, Instant createdAt) {
          this.id = id;
          this.type = type;
          this.userName = userName;
          this.projectId = projectId;
          this.email = email;
          this.createdAt = createdAt;
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

     public String getProjectId() {
          return projectId;
     }

     public void setProjectId(String projectId) {
          this.projectId = projectId;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public ReportEntity(int type, String userName, String projectId, String email) {
          this.type = type;
          this.userName = userName;
          this.projectId = projectId;
          this.email = email;
     }

     public ReportEntity() {
     }
}
