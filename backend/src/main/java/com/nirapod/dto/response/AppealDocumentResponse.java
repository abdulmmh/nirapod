package com.nirapod.dto.response;

import com.nirapod.model.AppealDocument;
import java.time.LocalDateTime;

public class AppealDocumentResponse {

    private Long          id;
    private Long          appealId;
    private String        fileName;
    private String        originalFileName;
    private String        fileType;
    private Long          fileSize;
    private String        description;
    private String        uploadedBy;
    private String        uploadedByName;
    private LocalDateTime uploadedAt;
    private String        downloadUrl;

    public static AppealDocumentResponse from(AppealDocument d) {
        AppealDocumentResponse r = new AppealDocumentResponse();
        r.id               = d.getId();
        r.appealId         = d.getAppealId();
        r.fileName         = d.getFileName();
        r.originalFileName = d.getOriginalFileName();
        r.fileType         = d.getFileType();
        r.fileSize         = d.getFileSize();
        r.description      = d.getDescription();
        r.uploadedBy       = d.getUploadedBy();
        r.uploadedAt       = d.getUploadedAt();
        r.downloadUrl      = "/api/my-portal/appeals/"
                           + d.getAppealId() + "/documents/" + d.getId() + "/download";
        return r;
    }

    public Long getId()                   { return id; }
    public Long getAppealId()             { return appealId; }
    public String getFileName()           { return fileName; }
    public String getOriginalFileName()   { return originalFileName; }
    public String getFileType()           { return fileType; }
    public Long getFileSize()             { return fileSize; }
    public String getDescription()        { return description; }
    public String getUploadedBy()         { return uploadedBy; }
    public String getUploadedByName()     { return uploadedByName; }
    public void setUploadedByName(String v) { this.uploadedByName = v; }
    public LocalDateTime getUploadedAt()  { return uploadedAt; }
    public String getDownloadUrl()        { return downloadUrl; }
}