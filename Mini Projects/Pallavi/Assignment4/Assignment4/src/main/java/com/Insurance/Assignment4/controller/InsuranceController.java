package com.Insurance.Assignment4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Insurance.Assignment4.model.Policy;
import com.Insurance.Assignment4.repository.PolicyRepository;
import com.Insurance.Assignment4.service.InsuranceService;
import com.opencsv.exceptions.CsvException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private PolicyRepository policyRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPolicies(@RequestParam("file") MultipartFile file) throws CsvException {
        insuranceService.uploadPoliciesFromCSV(file);
        return ResponseEntity.ok("Policies uploaded successfully");
    }


    @PostMapping("/claim/life")
    public ResponseEntity<String> processLifeInsuranceClaim(@RequestParam String policyNumber) {
        insuranceService.processLifeInsuranceClaim(policyNumber);
        return ResponseEntity.ok("Life insurance claim processed successfully");
    }

    @PostMapping("/claim/motor")
    public ResponseEntity<String> processMotorInsuranceClaim(@RequestParam String policyNumber) {
        insuranceService.processMotorInsuranceClaim(policyNumber);
        return ResponseEntity.ok("Motor insurance claim processed successfully");
    }

    @GetMapping("/report")
    public ResponseEntity<List<Policy>> generatePolicyReport() {
        List<Policy> policyReport = insuranceService.generatePolicyReport();
        return ResponseEntity.ok(policyReport);
    }
    @GetMapping("/report/csv")
    public void generatePolicyReportCsv(HttpServletResponse response) {
        insuranceService.generatePolicyReportCsv(response);
    }
    
}