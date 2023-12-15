package com.Insurance.Assignment4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Insurance.Assignment4.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByClaimStatus(String claimStatus);
    Policy findByPolicyNumber(String policyNumber);
}
