package com.nirapod.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "aitDocument")
@Table(name = "ait_documents", indexes = {
    @Index(name = "idx_ait_documents_ait_record_id", columnList = "ait_record_id"),
    @Index(name = "idx_ait_documents_uploaded_at", columnList = "uploaded_at")
})
public class AitDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ait_record_id", nullable = false)
  private AitRecord aitRecord;

  @Column(name = "file_name", nullable = false, length = 255)
  private String fileName;

  @Column(name = "file_path", nullable = false, length = 500)
  private String filePath;

  @Column(name = "file_type", length = 100)
  private String fileType;

  @Column(name = "file_size")
  private Long fileSize;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uploaded_by", nullable = false)
  private User uploadedBy;

  @CreationTimestamp
  @Column(name = "uploaded_at", nullable = false, updatable = false)
  private LocalDateTime uploadedAt;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  public AitDocument() {
  }

  public AitDocument(Long id, AitRecord aitRecord, String fileName, String filePath, String fileType,
      Long fileSize, User uploadedBy, LocalDateTime uploadedAt, Boolean isDeleted) {
    this.id = id;
    this.aitRecord = aitRecord;
    this.fileName = fileName;
    this.filePath = filePath;
    this.fileType = fileType;
    this.fileSize = fileSize;
    this.uploadedBy = uploadedBy;
    this.uploadedAt = uploadedAt;
    this.isDeleted = isDeleted;
  }

  @PrePersist
  protected void onCreate() {
    if (isDeleted == null) {
      isDeleted = false;
    }
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AitRecord getAitRecord() {
    return aitRecord;
  }

  public void setAitRecord(AitRecord aitRecord) {
    this.aitRecord = aitRecord;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public User getUploadedBy() {
    return uploadedBy;
  }

  public void setUploadedBy(User uploadedBy) {
    this.uploadedBy = uploadedBy;
  }

  public LocalDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(LocalDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
