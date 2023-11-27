import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VehicleBookingSystem {
    static List<Vehicle> vehicles = new ArrayList<>();
    static List<Trip> trips = new ArrayList<>();
    static Connection connection;

    public static void main(String[] args) {
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
            
            String url = "jdbc:mysql://localhost:3306/assignment";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);

            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            
            statement.execute("CREATE TABLE IF NOT EXISTS custom_vehicles (" +
                    "registration VARCHAR(50) PRIMARY KEY," +
                    "type VARCHAR(50)," +
                    "manufacturer VARCHAR(50)," +
                    "capacity INT," +
                    "totalTrips INT," +
                    "totalDistance DOUBLE)");

            
            statement.execute("CREATE TABLE IF NOT EXISTS custom_trips (" +
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
                "INSERT INTO custom_vehicles VALUES (?, ?, ?, ?, 0, 0)")) {
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
                "INSERT INTO custom_trips (vehicleRegistration, startKilometer, endKilometer) VALUES (?, ?, ?)")) {
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
