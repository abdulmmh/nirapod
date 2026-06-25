package com.nirapod.dto.response;

import com.nirapod.model.AitDocument;

import java.time.LocalDateTime;

public class AitDocumentDto {
  
  private Long id;
  private String fileName;
  private String fileType;
  private Long fileSize;
  private String uploadedBy;
  private LocalDateTime uploadedAt;

  public static AitDocumentDto from(AitDocument entity) {
    AitDocumentDto dto = new AitDocumentDto();
    dto.setId(entity.getId());
    dto.setFileName(entity.getFileName());
    dto.setFileType(entity.getFileType());
    dto.setFileSize(entity.getFileSize());
    dto.setUploadedBy(entity.getUploadedBy().getFullName());
    dto.setUploadedAt(entity.getUploadedAt());
    return dto;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
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

  public String getUploadedBy() {
    return uploadedBy;
  }

  public void setUploadedBy(String uploadedBy) {
    this.uploadedBy = uploadedBy;
  }

  public LocalDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(LocalDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

}
