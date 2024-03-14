import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Policy {
    String policyNumber;
    String policyType;
    int sumInsured;
    String issuedDate;
    String maturityDate;
    String claimStatus;
    boolean premiumPaid;
}

class Claim {
    String claimID;
    int claimAmount;
    String claimDate;
    String claimStatus;
    String policyNumber;
    String additionalDocs;

}

public class InsuranceManagementSystem {
    private static List<Policy> policies = new ArrayList<>();
    private static List<Claim> claims = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 for Policy Upload");
            System.out.println("Enter 2 for Life Insurance Claim");
            System.out.println("Enter 3 for Motor Insurance Claim");
            System.out.println("Enter 4 for Policy Report details generation");
            System.out.println("Enter 5 to exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    uploadPoliciesFromCSV();
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
                case 5:
                    System.out.println("Exiting the program.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void uploadPoliciesFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("policy_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Policy policy = new Policy();
                policy.policyNumber = parts[0];
                policy.policyType = parts[1];
                policy.sumInsured = Integer.parseInt(parts[2]);
                policy.issuedDate = parts[3];
                policies.add(policy);
            }
            System.out.println("Policies uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processLifeInsuranceClaim() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Policy Number for Life Insurance Claim:");
        String policyNumber = scanner.nextLine();

        Policy policy = findPolicy(policyNumber);
        if (policy == null) {
            System.out.println("Policy not found.");
            return;
        }

        if (!policy.premiumPaid) {
            System.out.println("Premium not paid. Cannot process the claim.");
            return;
        }

        System.out.println("Enter the Maturity Date (in the format DD/MM/YYYY):");
        String maturityDate = scanner.nextLine();

        if (maturityDate.compareTo(policy.maturityDate) > 0) {
            System.out.println("Policy maturity date passed. Cannot process the claim.");
            return;
        }

        System.out.println("Is the Death Certificate available? (yes/no):");
        String deathCertificateAvailable = scanner.nextLine().toLowerCase();

        if (!deathCertificateAvailable.equals("yes")) {
            System.out.println("Death Certificate is required for Life Insurance Claim.");
            return;
        }

        Claim claim = new Claim();
        claim.policyNumber = policyNumber;
        claim.claimStatus = "Pending";
        claims.add(claim);

        System.out.println("Life Insurance Claim processed successfully.");
    }

    private static void processMotorInsuranceClaim() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Policy Number for Motor Insurance Claim:");
        String policyNumber = scanner.nextLine();

        Policy policy = findPolicy(policyNumber);
        if (policy == null) {
            System.out.println("Policy not found.");
            return;
        }

        if (!policy.premiumPaid) {
            System.out.println("Premium not paid. Cannot process the claim.");
            return;
        }

        System.out.println("Enter the Maturity Date (in the format DD/MM/YYYY):");
        String maturityDate = scanner.nextLine();

        if (maturityDate.compareTo(policy.maturityDate) > 0) {
            System.out.println("Policy maturity date passed. Cannot process the claim.");
            return;
        }

        System.out.println("Is the Vehicle Report available? (yes/no):");
        String vehicleReportAvailable = scanner.nextLine().toLowerCase();

        if (!vehicleReportAvailable.equals("yes")) {
            System.out.println("Vehicle Report is required for Motor Insurance Claim.");
            return;
        }


        Claim claim = new Claim();
        claim.policyNumber = policyNumber;
        claim.claimStatus = "Pending";
        claims.add(claim);

        System.out.println("Motor Insurance Claim processed successfully.");
    }

    private static void generatePolicyReport() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("policy_report.csv"))) {
            writer.write("Policy_number,Policy_Type,Total_Amount_Claimed,Claim_Date\n");

            for (Policy policy : policies) {
                if (claimMade(policy.policyNumber)) {
                    int totalAmountClaimed = calculateTotalAmountClaimed(policy.policyNumber);
                    String claimDate = getClaimDate(policy.policyNumber);

                    writer.write(policy.policyNumber + "," + policy.policyType + "," + totalAmountClaimed + "," + claimDate + "\n");
                }
            }

            System.out.println("Policy report generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateTotalAmountClaimed(String policyNumber) {
        int totalAmountClaimed = 0;
        for (Claim claim : claims) {
            if (claim.policyNumber.equals(policyNumber) && claim.claimStatus.equals("Completed")) {
                totalAmountClaimed += claim.claimAmount;
            }
        }
        return totalAmountClaimed;
    }

    private static String getClaimDate(String policyNumber) {
        for (Claim claim : claims) {
            if (claim.policyNumber.equals(policyNumber) && (claim.claimStatus.equals("Completed") || claim.claimStatus.equals("Pending"))) {
                return claim.claimDate;
            }
        }
        return "N/A";
    }

    private static boolean claimMade(String policyNumber) {
        for (Claim claim : claims) {
            if (claim.policyNumber.equals(policyNumber) && (claim.claimStatus.equals("Completed") || claim.claimStatus.equals("Pending"))) {
                return true;
            }
        }
        return false;
    }

    private static Policy findPolicy(String policyNumber) {
        for (Policy policy : policies) {
            if (policy.policyNumber.equals(policyNumber)) {
                return policy;
            }
        }
        return null;
    }
}

