package com.Insurance.Assignment4.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Insurance.Assignment4.model.Claim;
import com.Insurance.Assignment4.model.Policy;
import com.Insurance.Assignment4.repository.ClaimRepository;
import com.Insurance.Assignment4.repository.PolicyRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class InsuranceService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ClaimRepository claimRepository;

    public void processLifeInsuranceClaim(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        if (policy != null && "Life".equalsIgnoreCase(policy.getPolicyType())) {
            Claim claim = claimRepository.findByPolicy(policy);
            if (claim != null && "Death Certificate".equalsIgnoreCase(claim.getAdditionalDocs())) {
                // Process life insurance claim logic
                claim.setClaimStatus("Completed");
                claimRepository.save(claim);
            } else {
                throw new RuntimeException("Life insurance claim requires a Death Certificate.");
            }
        } else {
            throw new RuntimeException("Invalid policy for life insurance claim.");
        }
    }

    public void processMotorInsuranceClaim(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        if (policy != null && "Motor".equalsIgnoreCase(policy.getPolicyType())) {
            Claim claim = claimRepository.findByPolicy(policy);
            if (claim != null && "Vehicle Report".equalsIgnoreCase(claim.getAdditionalDocs())) {
                // Process motor insurance claim logic
                claim.setClaimStatus("Completed");
                claimRepository.save(claim);
            } else {
                throw new RuntimeException("Motor insurance claim requires a Vehicle Report.");
            }
        } else {
            throw new RuntimeException("Invalid policy for motor insurance claim.");
        }
    }

    public List<Policy> generatePolicyReport() {
        return policyRepository.findByClaimStatus("Claimed");
    }
    String file = "/Assignment4/src/main/resources/templates/policy.csv";
    public void uploadPoliciesFromCSV(MultipartFile file) throws CsvException {
    	
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            // Assuming the CSV file format: Policy_Number,Policy_type,Sum_Insured,Policy_IssuedDate
            List<String[]> lines = reader.readAll();

            for (String[] line : lines) {
                if (line.length >= 4) {
                    Policy policy = new Policy();
                    policy.setPolicyNumber(line[0]);
                    policy.setPolicyType(line[1]);
                    policy.setSumInsured(Double.parseDouble(line[2]));
                    policy.setPolicyIssuedDate(LocalDate.parse(line[3]));

                    // You may want to set other properties of the Policy entity here

                    policyRepository.save(policy);
                } else {
                    // Handle incomplete or incorrect data in the CSV file
                    throw new RuntimeException("Invalid CSV format");
                }
            }
        } catch (IOException e) {
            // Handle file reading exception
            throw new RuntimeException("Error reading CSV file", e);
        }
        
}
    public void generatePolicyReportCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=policy_report.csv");

        try (PrintWriter writer = response.getWriter()) {
            // Write CSV header
            writer.println("Policy_number,Policy_Type,Total_Amount_Claimed,Claim_Date");

            // Get policies with claim status "Claimed"
            List<Policy> policiesWithClaims = policyRepository.findByClaimStatus("Claimed");

            // Write data for each policy with claims
            for (Policy policy : policiesWithClaims) {
                Claim claim = claimRepository.findByPolicy(policy);
                if (claim != null) {
                    // Adjust this part based on your actual claim and policy properties
                    writer.println(
                            policy.getPolicyNumber() + "," +
                            policy.getPolicyType() + "," +
                            claim.getClaimAmount() + "," +
                            claim.getClaimDate()
                    );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV report", e);
        }
    }
}