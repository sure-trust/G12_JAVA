package com.Insurance.Assignment4.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;

import com.Insurance.Assignment4.service.InsuranceService;
import com.opencsv.exceptions.CsvException;

public class InsuranceApp {
	@Value("${csv.file.path}")
	private String csvFilePath;
    private static final Scanner scanner = new Scanner(System.in);
    private static final InsuranceService insuranceservice=new InsuranceService();

    public static void main(String[] args) throws Exception {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    uploadPolicies();
                    break;
                case 2:
                    processLifeInsuranceClaim();
                    break;
                case 3:
                    processMotorInsuranceClaim();
                    break;
                case 4:
                    generatePolicyReport();
                    break;
                case 0:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("===== Insurance Management System =====");
        System.out.println("Enter 1 for Policy Upload");
        System.out.println("Enter 2 for Life Insurance Claim");
        System.out.println("Enter 3 for Motor Insurance Claim");
        System.out.println("Enter 4 for Policy Report details generation");
        System.out.println("Enter 0 to exit");
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void uploadPolicies() throws CsvException {
        // Implement policy upload logic
        insuranceservice.uploadPoliciesFromCSV(null);
        System.out.println("Policies uploaded successfully.");
    }

    private static void processLifeInsuranceClaim() {
        System.out.print("Enter Policy Number for Life Insurance Claim: ");
        String policyNumber = scanner.next();
        insuranceservice.processLifeInsuranceClaim(policyNumber);
        System.out.println("Life insurance claim processed successfully.");
    }

    private static void processMotorInsuranceClaim() {
        System.out.print("Enter Policy Number for Motor Insurance Claim: ");
        String policyNumber = scanner.next();
        insuranceservice.processMotorInsuranceClaim(policyNumber);
        System.out.println("Motor insurance claim processed successfully.");
    }

    private static void generatePolicyReport() {
        insuranceservice.generatePolicyReport();
        System.out.println("Policy report generated successfully.");
    }
}
