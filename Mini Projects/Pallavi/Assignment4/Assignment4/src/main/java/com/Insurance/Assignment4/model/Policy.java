package com.Insurance.Assignment4.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String policyNumber;
    private String policyType;
    private double sumInsured;
    private LocalDate policyIssuedDate;
    private LocalDate policyMaturityDate;
    private String claimStatus;
    private String premiumPaidStatus;
	public Policy() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Policy(Long id, String policyNumber, String policyType, double sumInsured, LocalDate policyIssuedDate,
			LocalDate policyMaturityDate, String claimStatus, String premiumPaidStatus) {
		super();
		this.id = id;
		this.policyNumber = policyNumber;
		this.policyType = policyType;
		this.sumInsured = sumInsured;
		this.policyIssuedDate = policyIssuedDate;
		this.policyMaturityDate = policyMaturityDate;
		this.claimStatus = claimStatus;
		this.premiumPaidStatus = premiumPaidStatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public double getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(double sumInsured) {
		this.sumInsured = sumInsured;
	}
	public LocalDate getPolicyIssuedDate() {
		return policyIssuedDate;
	}
	public void setPolicyIssuedDate(LocalDate policyIssuedDate) {
		this.policyIssuedDate = policyIssuedDate;
	}
	public LocalDate getPolicyMaturityDate() {
		return policyMaturityDate;
	}
	public void setPolicyMaturityDate(LocalDate policyMaturityDate) {
		this.policyMaturityDate = policyMaturityDate;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getPremiumPaidStatus() {
		return premiumPaidStatus;
	}
	public void setPremiumPaidStatus(String premiumPaidStatus) {
		this.premiumPaidStatus = premiumPaidStatus;
	}
	@Override
	public String toString() {
		return "Policy [id=" + id + ", policyNumber=" + policyNumber + ", policyType=" + policyType + ", sumInsured="
				+ sumInsured + ", policyIssuedDate=" + policyIssuedDate + ", policyMaturityDate=" + policyMaturityDate
				+ ", claimStatus=" + claimStatus + ", premiumPaidStatus=" + premiumPaidStatus + "]";
	}
    

    // Getters and setters
}