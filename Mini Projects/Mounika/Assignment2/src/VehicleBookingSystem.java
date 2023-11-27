import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Vehicle {
    String registration;
    String type;
    String manufacturer;
    int capacity;
    int totalTrips;
    double totalDistance;

    Vehicle(String registration, String type, String manufacturer, int capacity) {
        this.registration = registration;
        this.type = type;
        this.manufacturer = manufacturer;
        this.capacity = capacity;
        this.totalTrips = 0;
        this.totalDistance = 0;
    }
}

class Trip {
    String vehicleRegistration;
    double startKilometer;
    double endKilometer;

    Trip(String vehicleRegistration, double startKilometer, double endKilometer) {
        this.vehicleRegistration = vehicleRegistration;
        this.startKilometer = startKilometer;
        this.endKilometer = endKilometer;
    }
}

public class VehicleBookingSystem {
    static List<Vehicle> vehicles = new ArrayList<>();
    static List<Trip> trips = new ArrayList<>();
    static Connection connection;

    public static void main(String[] args) {
        // Initialize the database connection
        initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while (true) {
            System.out.println("Enter 1 for Vehicle Registration");
            System.out.println("Enter 2 for Vehicle Allocation Request");
            System.out.println("Enter 3 for Multiple Allocation Requests in Parallel");
            System.out.println("Enter 4 for Vehicle Usage Report");
            System.out.println("Enter 5 for csv Report");
            System.out.println("Enter 0 to exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    registerVehicle(scanner);
                    break;
                case 2:
                    allocateVehicle(scanner);
                    break;
                case 3:
                    parallelVehicleAllocation(executorService, scanner);
                    break;
                case 4:
                    generateReport();
                    break;
                case 0:
                    executorService.shutdown();
                    closeDatabase();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Replace the placeholders with your actual database information
            String url = "jdbc:mysql://localhost:3306/assignment";
            String username = "root";
            String password = "@Mouni2003";
            connection = DriverManager.getConnection(url, username, password);

            // Create tables if not exists
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Vehicle table
            statement.execute("CREATE TABLE IF NOT EXISTS vehicles (" +
                    "registration VARCHAR(50) PRIMARY KEY," +
                    "type VARCHAR(50)," +
                    "manufacturer VARCHAR(50)," +
                    "capacity INT," +
                    "totalTrips INT," +
                    "totalDistance DOUBLE)");

            // Trip table
            statement.execute("CREATE TABLE IF NOT EXISTS trips (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "vehicleRegistration VARCHAR(50)," +
                    "startKilometer DOUBLE," +
                    "endKilometer DOUBLE)");
        }
    }

    private static void registerVehicle(Scanner scanner) {
        System.out.println("Enter Vehicle Registration: ");
        String registration = scanner.next();
        System.out.println("Enter Vehicle Type: ");
        String type = scanner.next();
        System.out.println("Enter Vehicle Manufacturer: ");
        String manufacturer = scanner.next();
        System.out.println("Enter Vehicle Capacity: ");
        int capacity = scanner.nextInt();

        Vehicle vehicle = new Vehicle(registration, type, manufacturer, capacity);
        vehicles.add(vehicle);
    

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO vehicles VALUES (?, ?, ?, ?, 0, 0)")) {
            preparedStatement.setString(1, registration);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, manufacturer);
            preparedStatement.setInt(4, capacity);
            preparedStatement.executeUpdate();
            System.out.println("Vehicle registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void allocateVehicle(Scanner scanner) {
        System.out.println("Enter Vehicle Type required: ");
        String requiredType = scanner.next();
        System.out.println("Enter Start Kilometer: ");
        double startKilometer = scanner.nextDouble();
        System.out.println("Enter End Kilometer: ");
        double endKilometer = scanner.nextDouble();


        System.out.println("Vehicle allocated successfully!");

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO trips (vehicleRegistration, startKilometer, endKilometer) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, requiredType);
            preparedStatement.setDouble(2, startKilometer);
            preparedStatement.setDouble(3, endKilometer);
            preparedStatement.executeUpdate();
            System.out.println("Vehicle allocated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void parallelVehicleAllocation(ExecutorService executorService, Scanner scanner) {
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> allocateVehicle(scanner));
        }
    }

    private static void generateReport() {
       System.out.println("Vehicle Registration | Vehicle Type | Total Trips | Total Distance");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.registration + " | " + vehicle.type + " | "
                    + vehicle.totalTrips + " | " + vehicle.totalDistance);
        }
    }

    private static void closeDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
	

