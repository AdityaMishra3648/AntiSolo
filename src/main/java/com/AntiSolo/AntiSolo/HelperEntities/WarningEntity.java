package com.AntiSolo.AntiSolo.HelperEntities;

public class WarningEntity {
    private String projectId;
    private String message;
    private String reportFrom;

    public WarningEntity(String projectId, String message, String reportFrom) {
        this.projectId = projectId;
        this.message = message;
        this.reportFrom = reportFrom;
    }

    public String getReportFrom() {
        return reportFrom;
    }

    public void setReportFrom(String reportFrom) {
        this.reportFrom = reportFrom;
    }

    public WarningEntity(String projectId, String message) {
        this.projectId = projectId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public WarningEntity() {
    }
}
