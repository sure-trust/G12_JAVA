import java.io.*;
import java.text.*;
import java.util.*;

class Policy{
    String policyNumber;
    String policyType;
    double sumInsured;
    Date issuedDate;

    Policy(String policyNumber, String policyType, double sumInsured, Date issuedDate) {
        this.policyNumber = policyNumber;
        this.policyType = policyType;
        this.sumInsured = sumInsured;
        this.issuedDate = issuedDate;
    }
}

class Claim {
    int claimID;
    double claimAmount;
    Date claimDate;
    String claimStatus;
    String policyNumber;
    String additionalDocs;

    Claim(int claimID, double claimAmount, Date claimDate, String claimStatus, String policyNumber, String additionalDocs) {
        this.claimID = claimID;
        this.claimAmount = claimAmount;
        this.claimDate = claimDate;
        this.claimStatus = claimStatus;
        this.policyNumber = policyNumber;
        this.additionalDocs = additionalDocs;
    }
}

public class Insurance_Management_System {
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
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void uploadPoliciesFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("policies.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date issuedDate = dateFormat.parse(values[3]);
                policies.add(new Policy(values[0], values[1], Double.parseDouble(values[2]), issuedDate));
            }
            System.out.println("Policies uploaded successfully.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void processLifeInsuranceClaim() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Policy Number for Life Insurance Claim:");
        String policyNumber = scanner.nextLine();

        boolean hasDeathCertificate = checkForDeathCertificate(policyNumber);

        if (hasDeathCertificate) {
            System.out.println("Life Insurance Claim processed successfully.");
        } else {
            System.out.println("Error: Death Certificate required for Life Insurance Claim.");
        }
    }

    private static boolean checkForDeathCertificate(String policyNumber) {
  
        Scanner scanner = new Scanner(System.in);
        System.out.println("Has Death Certificate been provided? (Enter 'yes' or 'no'):");
        String response = scanner.nextLine().toLowerCase();
        return response.equals("yes");
    }

    private static void processMotorInsuranceClaim() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Policy Number for Motor Insurance Claim:");
        String policyNumber = scanner.nextLine();

        boolean hasVehicleReport = checkForVehicleReport(policyNumber);

        if (hasVehicleReport) {
            System.out.println("Motor Insurance Claim processed successfully.");
        } else {
            System.out.println("Error: Vehicle Report required for Motor Insurance Claim.");
        }
    }

    private static boolean checkForVehicleReport(String policyNumber) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Has Vehicle Report been provided? (Enter 'yes' or 'no'):");
        String response = scanner.nextLine().toLowerCase();
        return response.equals("yes");
    }

    private static void generatePolicyReport() {
    
        try (FileWriter writer = new FileWriter("policy_report.csv")) {
            writer.append("Policy_number,Policy_Type,Total_Amount_Claimed,Claim_Date\n");

            for (Claim claim : claims) {
                if (claim.claimStatus.equals("Completed") || claim.claimStatus.equals("Pending")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String claimDateStr = dateFormat.format(claim.claimDate);

                    writer.append(claim.policyNumber)
                            .append(",").append(getPolicyType(claim.policyNumber))
                            .append(",").append(String.valueOf(claim.claimAmount))
                            .append(",").append(claimDateStr)
                            .append("\n");
                }
            }

            System.out.println("Policy Report generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPolicyType(String policyNumber) {
  
        for (Policy policy : policies) {
            if (policy.policyNumber.equals(policyNumber)) {
                return policy.policyType;
            }
        }
        return "Unknown";
    }
}

