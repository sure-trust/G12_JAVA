package com.Insurance.Assignment4.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    private double claimAmount;
    private LocalDate claimDate;
    private String claimStatus;
    private String additionalDocs;

    @ManyToOne
    @JoinColumn(name = "id")
    private Policy policy;

	public Claim() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Claim(Long cid, double claimAmount, LocalDate claimDate, String claimStatus, String additionalDocs,
			Policy policy) {
		super();
		this.cid = cid;
		this.claimAmount = claimAmount;
		this.claimDate = claimDate;
		this.claimStatus = claimStatus;
		this.additionalDocs = additionalDocs;
		this.policy = policy;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public LocalDate getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(LocalDate claimDate) {
		this.claimDate = claimDate;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getAdditionalDocs() {
		return additionalDocs;
	}

	public void setAdditionalDocs(String additionalDocs) {
		this.additionalDocs = additionalDocs;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	@Override
	public String toString() {
		return "Claim [cid=" + cid + ", claimAmount=" + claimAmount + ", claimDate=" + claimDate + ", claimStatus="
				+ claimStatus + ", additionalDocs=" + additionalDocs + ", policy=" + policy + "]";
	}

    // Getters and setters
}