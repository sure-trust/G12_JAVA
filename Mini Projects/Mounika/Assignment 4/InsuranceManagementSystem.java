import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
public class InsuranceManagementSystem {

    static List<Policy> policies = new ArrayList<>();
    static List<Claim> claims = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 for Policy Upload");
            System.out.println("Enter 2 for Life Insurance Claim");
            System.out.println("Enter 3 for Motor Insurance Claim");
            System.out.println("Enter 4 for Policy Report details generation");
            System.out.println("Enter 5 to exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    System.out.println("Exiting program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void uploadPoliciesFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dandu.csv"))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String policyNumber = parts[0];
                String policyType = parts[1];
                double sumInsured = Double.parseDouble(parts[2]);
                Date policyIssuedDate = dateFormat.parse(parts[3]);
                String claimstatus = parts[4];
                

                Policy policy = new Policy(policyNumber, policyType, sumInsured, policyIssuedDate,claimstatus);
                policies.add(policy);
            }

            System.out.println("Policies uploaded successfully.");
        } catch (IOException | ParseException e) {
            System.out.println("Error uploading policies: " + e.getMessage());
        }
    }

    private static void processLifeInsuranceClaim() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Policy Number for Life Insurance Claim:");
        String policyNumber = scanner.nextLine();

        Policy policy = findPolicy(policyNumber);
        if (policy != null && "Life".equalsIgnoreCase(policy.policyType)) {
            System.out.println("Enter Death Certificate (Y/N):");
            String hasDeathCertificate = scanner.nextLine();

            if ("Y".equalsIgnoreCase(hasDeathCertificate)) {
                // Process the life insurance claim
                System.out.println("Enter Claim Amount:");
                double claimedAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                Date claimDate = new Date(); // Current date
                String claimStatus = "Pending"; // You may set it to "Completed" based on your logic

                Claim claim = new Claim(policyNumber, "Life", claimDate, claimedAmount, claimStatus);
                claims.add(claim);

                System.out.println("Life Insurance Claim processed successfully.");
            } else {
                System.out.println("Error: Death Certificate is required for Life Insurance Claim.");
            }
        } else {
            System.out.println("Error: Invalid policy number or policy type is not Life Insurance.");
        }
    }

    private static void processMotorInsuranceClaim() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Policy Number for Motor Insurance Claim:");
        String policyNumber = scanner.nextLine();

        Policy policy = findPolicy(policyNumber);
        if (policy != null && "Motor".equalsIgnoreCase(policy.policyType)) {
            System.out.println("Enter Vehicle Report (Y/N):");
            String hasVehicleReport = scanner.nextLine();

            if ("Y".equalsIgnoreCase(hasVehicleReport)) {
                // Process the motor insurance claim
                System.out.println("Enter Claim Amount:");
                double claimedAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                Date claimDate = new Date(); // Current date
                String claimStatus = "Pending"; // You may set it to "Completed" based on your logic

                Claim claim = new Claim(policyNumber, "Motor", claimDate, claimedAmount, claimStatus);
                claims.add(claim);

                System.out.println("Motor Insurance Claim processed successfully.");
            } else {
                System.out.println("Error: Vehicle Report is required for Motor Insurance Claim.");
            }
        } else {
            System.out.println("Error: Invalid policy number or policy type is not Motor Insurance.");
        }
    }

    private static void generatePolicyReport() {
        try (FileWriter writer = new FileWriter("policy_report.csv")) {
            writer.write("Policy_number,Policy_Type,Total_Amount_Claimed,Claim_Date\n");

            for (Claim claim : claims) {
                if ("Completed".equalsIgnoreCase(claim.claimStatus) || "Pending".equalsIgnoreCase(claim.claimStatus)) {
                    writer.write(String.format("%s,%s,%.2f,%tF\n",
                            claim.policyNumber, claim.claimType, claim.claimedAmount, claim.claimDate));
                }
            }

            System.out.println("Policy Report generated successfully.");
        } catch (IOException e) {
            System.out.println("Error generating policy report: " + e.getMessage());
        }
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

class Policy {
    String policyNumber;
    String policyType;
    double sumInsured;
    Date policyIssuedDate;
    String claimstatus;

    public Policy(String policyNumber, String policyType, double sumInsured, Date policyIssuedDate, String claimstatus) {
        this.policyNumber = policyNumber;
        this.policyType = policyType;
        this.sumInsured = sumInsured;
        this.policyIssuedDate = policyIssuedDate;
        this.claimstatus =claimstatus;
    }
}

class Claim {
    String policyNumber;
    String claimType;
    Date claimDate;
    double claimedAmount;
    String claimStatus;

    public Claim(String policyNumber, String claimType, Date claimDate, double claimedAmount, String claimStatus) {
        this.policyNumber = policyNumber;
        this.claimType = claimType;
        this.claimDate = claimDate;
        this.claimedAmount = claimedAmount;
        this.claimStatus = claimStatus;
    }
}


