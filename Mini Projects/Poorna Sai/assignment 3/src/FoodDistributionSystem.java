import java.sql.*;
import java.util.Scanner;

public class FoodDistributionSystem {
    private static final String DATABASE_URL = "jdbc:sqlite:food_distribution.db";

    public static void main(String[] args) {
        createTables();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 for Food Shelf Life Upload");
            System.out.println("Enter 2 for Food Available Details");
            System.out.println("Enter 3 for Food Wanted Request");
            System.out.println("Enter 4 for Food Allocation");
            System.out.println("Enter 5 for Food Distribution Usage Report");
            System.out.println("Enter 0 to exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    uploadFoodShelfLife();
                    break;
                case 2:
                    enterFoodAvailableDetails();
                    break;
                case 3:
                    enterFoodWantedRequest();
                    break;
                case 4:
                    allocateFood();
                    break;
                case 5:
                    generateFoodDistributionReport();
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void createTables() {
         try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS FoodShelfLife (" +
                    "foodName TEXT, " +
                    "foodType TEXT, " +
                    "shelfLife INTEGER, " +
                    "packaging TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS FoodRequest (" +
                    "reqId INTEGER PRIMARY KEY, " +
                    "foodNameWanted TEXT, " +
                    "totalQuantity INTEGER, " +
                    "requiredTime TEXT, " +
                    "requireLocation INTEGER, " +
                    "requestStatus TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS FoodAvailable (" +
                    "reqId INTEGER PRIMARY KEY, " +
                    "foodNameWanted TEXT, " +
                    "totalQuantity INTEGER, " +
                    "preparedTime TEXT, " +
                    "availableLocation INTEGER, " +
                    "allocationStatus TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void uploadFoodShelfLife() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO FoodShelfLife VALUES (?, ?, ?, ?)")) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Food Name:");
            String foodName = scanner.next();
            System.out.println("Enter Food Type:");
            String foodType = scanner.next();
            System.out.println("Enter Shelf Life (in days):");
            int shelfLife = scanner.nextInt();
            System.out.println("Enter Packaging:");
            String packaging = scanner.next();

            preparedStatement.setString(1, foodName);
            preparedStatement.setString(2, foodType);
            preparedStatement.setInt(3, shelfLife);
            preparedStatement.setString(4, packaging);

            preparedStatement.executeUpdate();

            System.out.println("Food Shelf Life uploaded successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void enterFoodAvailableDetails() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO FoodAvailable VALUES (?, ?, ?, ?, ?, ?)")) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Request ID:");
            int reqId = scanner.nextInt();
            System.out.println("Enter Food Name Wanted:");
            String foodNameWanted = scanner.next();
            System.out.println("Enter Total Quantity:");
            int totalQuantity = scanner.nextInt();
            System.out.println("Enter Prepared Time:");
            String preparedTime = scanner.next();
            System.out.println("Enter Available Location (Latitude-Longitude):");
            int availableLocation = scanner.nextInt();
            System.out.println("Enter Allocation Status:");
            String allocationStatus = scanner.next();

            preparedStatement.setInt(1, reqId);
            preparedStatement.setString(2, foodNameWanted);
            preparedStatement.setInt(3, totalQuantity);
            preparedStatement.setString(4, preparedTime);
            preparedStatement.setInt(5, availableLocation);
            preparedStatement.setString(6, allocationStatus);

            preparedStatement.executeUpdate();

            System.out.println("Food Available details entered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void enterFoodWantedRequest() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO FoodRequest VALUES (?, ?, ?, ?, ?, ?)")) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Request ID:");
            int reqId = scanner.nextInt();
            System.out.println("Enter Food Name Wanted:");
            String foodNameWanted = scanner.next();
            System.out.println("Enter Total Quantity:");
            int totalQuantity = scanner.nextInt();
            System.out.println("Enter Required Time:");
            String requiredTime = scanner.next();
            System.out.println("Enter Require Location (Latitude-Longitude):");
            int requireLocation = scanner.nextInt();
            System.out.println("Enter Request Status:");
            String requestStatus = scanner.next();

            preparedStatement.setInt(1, reqId);
            preparedStatement.setString(2, foodNameWanted);
            preparedStatement.setInt(3, totalQuantity);
            preparedStatement.setString(4, requiredTime);
            preparedStatement.setInt(5, requireLocation);
            preparedStatement.setString(6, requestStatus);

            preparedStatement.executeUpdate();

            System.out.println("Food Wanted request entered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void allocateFood() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            // Your logic for food allocation based on proximity, prepared time, and shelf life goes here

            // Example: Update allocation status in FoodAvailable and FoodRequest tables
            String updateFoodAvailable = "UPDATE FoodAvailable SET allocationStatus = 'allocated' WHERE preparedTime <= 'current_time'";
            statement.executeUpdate(updateFoodAvailable);

            String updateFoodRequest = "UPDATE FoodRequest SET requestStatus = 'fulfilled' WHERE reqId IN (SELECT reqId FROM FoodAvailable WHERE allocationStatus = 'allocated')";
            statement.executeUpdate(updateFoodRequest);

            System.out.println("Food allocated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateFoodDistributionReport() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            // Your logic for generating the food distribution report goes here

            // Example: Print or display the report
            ResultSet resultSet = statement.executeQuery("SELECT foodNameWanted, COUNT(reqId) AS Total_Requests, " +
                    "SUM(CASE WHEN requestStatus = 'fulfilled' THEN 1 ELSE 0 END) AS Total_served_before_expiry, " +
                    "SUM(CASE WHEN requestStatus = 'pending' THEN 1 ELSE 0 END) AS Food_wasted " +
                    "FROM FoodRequest GROUP BY foodNameWanted");

            while (resultSet.next()) {
                System.out.println("Food Name: " + resultSet.getString("foodNameWanted"));
                System.out.println("Total Requests: " + resultSet.getInt("Total_Requests"));
                System.out.println("Total Served Before Expiry: " + resultSet.getInt("Total_served_before_expiry"));
                System.out.println("Food Wasted: " + resultSet.getInt("Food_wasted"));
                System.out.println("-------------------------");
            }

            System.out.println("Food Distribution Usage Report generated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
