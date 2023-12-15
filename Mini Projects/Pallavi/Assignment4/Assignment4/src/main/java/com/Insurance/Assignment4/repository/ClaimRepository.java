package com.Insurance.Assignment4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Insurance.Assignment4.model.Claim;
import com.Insurance.Assignment4.model.Policy;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Claim findByPolicy(Policy policy);
}
