package com.nirapod.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "taxpayer")
@Table(name = "taxpayers")
public class Taxpayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tinNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taxpayer_type_id")
    private TaxpayerType taxpayerType;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_zone_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "taxCircles", "district"})
    private TaxZone taxZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_circle_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "taxZone"})
    private TaxCircle taxCircle;

    private String fullName;
    private String nid;
    private String fathersName;
    private String mothersName;
    private LocalDate dateOfBirth;
    private String gender;
    private String profession;

    private String companyName;
    private LocalDate incorporationDate;
    private String tradeLicenseNo;
    private String rjscNo;
    private String natureOfBusiness;
    private String authorizedPersonName;
    private String authorizedPersonNid;
    private String authorizedPersonDesignation;

    private String email;
    private String phone;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "division",    column = @Column(name = "present_division")),
        @AttributeOverride(name = "district",    column = @Column(name = "present_district")),
        @AttributeOverride(name = "thana",       column = @Column(name = "present_thana")),
        @AttributeOverride(name = "roadVillage", column = @Column(name = "present_road_village"))
    })
    private Address presentAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "division",    column = @Column(name = "permanent_division")),
        @AttributeOverride(name = "district",    column = @Column(name = "permanent_district")),
        @AttributeOverride(name = "thana",       column = @Column(name = "permanent_thana")),
        @AttributeOverride(name = "roadVillage", column = @Column(name = "permanent_road_village"))
    })
    private Address permanentAddress;

    private Boolean sameAsPermanent;

    private String status;
    private LocalDate registrationDate;

    private String photoPath;

    @OneToMany(mappedBy = "taxpayer", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("taxpayer")
    private List<Business> businesses;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    private String approvalStatus;
    private String reviewNotes;
    private Long reviewedBy;

    @Column(updatable = true)
    private LocalDateTime reviewedAt;
    
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTinNumber() { return tinNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }

    public TaxpayerType getTaxpayerType() { return taxpayerType; }
    public void setTaxpayerType(TaxpayerType t) { this.taxpayerType = t; }

    // Zone & Circle
    public TaxZone getTaxZone() { return taxZone; }
    public void setTaxZone(TaxZone taxZone) { this.taxZone = taxZone; }

    public TaxCircle getTaxCircle() { return taxCircle; }
    public void setTaxCircle(TaxCircle taxCircle) { this.taxCircle = taxCircle; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getNid() { return nid; }
    public void setNid(String nid) { this.nid = nid; }

    public String getFathersName() { return fathersName; }
    public void setFathersName(String n) { this.fathersName = n; }

    public String getMothersName() { return mothersName; }
    public void setMothersName(String n) { this.mothersName = n; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate d) { this.dateOfBirth = d; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getProfession() { return profession; }
    public void setProfession(String p) { this.profession = p; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String c) { this.companyName = c; }

    public LocalDate getIncorporationDate() { return incorporationDate; }
    public void setIncorporationDate(LocalDate d) { this.incorporationDate = d; }

    public String getTradeLicenseNo() { return tradeLicenseNo; }
    public void setTradeLicenseNo(String t) { this.tradeLicenseNo = t; }

    public String getRjscNo() { return rjscNo; }
    public void setRjscNo(String r) { this.rjscNo = r; }

    public String getNatureOfBusiness() { return natureOfBusiness; }
    public void setNatureOfBusiness(String n) { this.natureOfBusiness = n; }

    public String getAuthorizedPersonName() { return authorizedPersonName; }
    public void setAuthorizedPersonName(String n) { this.authorizedPersonName = n; }

    public String getAuthorizedPersonNid() { return authorizedPersonNid; }
    public void setAuthorizedPersonNid(String n) { this.authorizedPersonNid = n; }

    public String getAuthorizedPersonDesignation() { return authorizedPersonDesignation; }
    public void setAuthorizedPersonDesignation(String d) { this.authorizedPersonDesignation = d; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Address getPresentAddress() { return presentAddress; }
    public void setPresentAddress(Address a) { this.presentAddress = a; }

    public Address getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(Address a) { this.permanentAddress = a; }

    public Boolean getSameAsPermanent() { return sameAsPermanent; }
    public void setSameAsPermanent(Boolean s) { this.sameAsPermanent = s; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate d) { this.registrationDate = d; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public List<Business> getBusinesses() { return businesses; }
    public void setBusinesses(List<Business> businesses) { this.businesses = businesses; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }

    public Long getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
	
    public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

}