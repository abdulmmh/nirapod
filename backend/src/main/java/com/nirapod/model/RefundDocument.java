package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "refundDocument")
@Table(name = "refund_documents")
public class RefundDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Refund refund;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;         // BANK_STATEMENT, ITR_ACKNOWLEDGMENT, etc.

    @Column(name = "document_name", nullable = false)
    private String documentName;         // display name

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;     // original uploaded filename

    @Column(name = "stored_filename", nullable = false)
    private String storedFilename;       // UUID-based stored filename

    @Column(name = "file_path")
    private String filePath;             // server-side path

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "content_type", length = 100)
    private String contentType;          // application/pdf, image/jpeg, etc.

    @Column(name = "verification_status", length = 20)
    private String verificationStatus = "PENDING";  // PENDING, VERIFIED, REJECTED

    @Column(name = "uploaded_by", length = 100)
    private String uploadedBy;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                          { return id; }
    public void setId(Long v)                    { this.id = v; }

    public Refund getRefund()                    { return refund; }
    public void setRefund(Refund v)              { this.refund = v; }

    public String getDocumentType()              { return documentType; }
    public void setDocumentType(String v)        { this.documentType = v; }

    public String getDocumentName()              { return documentName; }
    public void setDocumentName(String v)        { this.documentName = v; }

    public String getOriginalFilename()          { return originalFilename; }
    public void setOriginalFilename(String v)    { this.originalFilename = v; }

    public String getStoredFilename()            { return storedFilename; }
    public void setStoredFilename(String v)      { this.storedFilename = v; }

    public String getFilePath()                  { return filePath; }
    public void setFilePath(String v)            { this.filePath = v; }

    public Long getFileSizeBytes()               { return fileSizeBytes; }
    public void setFileSizeBytes(Long v)         { this.fileSizeBytes = v; }

    public String getContentType()               { return contentType; }
    public void setContentType(String v)         { this.contentType = v; }

    public String getVerificationStatus()        { return verificationStatus; }
    public void setVerificationStatus(String v)  { this.verificationStatus = v; }

    public String getUploadedBy()                { return uploadedBy; }
    public void setUploadedBy(String v)          { this.uploadedBy = v; }

    public LocalDateTime getUploadedAt()         { return uploadedAt; }
    public void setUploadedAt(LocalDateTime v)   { this.uploadedAt = v; }
}
