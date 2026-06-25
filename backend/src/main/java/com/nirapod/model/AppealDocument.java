package com.nirapod.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appeal_documents")
public class AppealDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appeal_id", nullable = false)
    private Long appealId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "description")
    private String description;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // Getters & Setters
    public Long getId()                       { return id; }
    public void setId(Long id)               { this.id = id; }
    public Long getAppealId()                 { return appealId; }
    public void setAppealId(Long v)           { this.appealId = v; }
    public String getFileName()               { return fileName; }
    public void setFileName(String v)         { this.fileName = v; }
    public String getOriginalFileName()       { return originalFileName; }
    public void setOriginalFileName(String v) { this.originalFileName = v; }
    public String getFileType()               { return fileType; }
    public void setFileType(String v)         { this.fileType = v; }
    public Long getFileSize()                 { return fileSize; }
    public void setFileSize(Long v)           { this.fileSize = v; }
    public String getFilePath()               { return filePath; }
    public void setFilePath(String v)         { this.filePath = v; }
    public String getDescription()            { return description; }
    public void setDescription(String v)      { this.description = v; }
    public String getUploadedBy()             { return uploadedBy; }
    public void setUploadedBy(String v)       { this.uploadedBy = v; }
    public LocalDateTime getUploadedAt()      { return uploadedAt; }
    public void setUploadedAt(LocalDateTime v){ this.uploadedAt = v; }
    public boolean isDeleted()                { return isDeleted; }
    public void setDeleted(boolean v)         { this.isDeleted = v; }
}