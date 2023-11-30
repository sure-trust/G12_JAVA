import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class InventoryManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 for Supply Details, 2 for Demand Details, 3 for Inventory Level, 4 for Generate CSV, or 0 to exit: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    loadSupplyDetails();
                    break;
                case 2:
                    loadDemandDetails();
                    break;
                case 3:
                    getInventoryLevel();
                    break;
                case 4:
                    generateCSV();
                    break;
                case 0:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment1", "root", "@Mouni2003");
    }

    private static void loadSupplyDetails() {
        try {
            Connection connection = getConnection();

            BufferedReader reader = new BufferedReader(new FileReader("Supply.csv"));
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                String Product_Code = parts[0];
                String Location = parts[1];
                int Supply_Qty = Integer.parseInt(parts[2]);

                // Parse date from string to java.sql.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date parsedDate = dateFormat.parse(parts[3]);
                java.sql.Date Supply_Date = new java.sql.Date(parsedDate.getTime());

                // Insert data into MySQL
                String sql = "INSERT INTO Supply (Product_Code, Location, Supply_Qty, Supply_Date) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, Product_Code);
                statement.setString(2, Location);
                statement.setInt(3, Supply_Qty);
                statement.setDate(4, Supply_Date);
                statement.executeUpdate();
            }

            reader.close();
            connection.close();

            System.out.println("Supply details loaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadDemandDetails() {
        try {
            Connection connection = getConnection();

            BufferedReader reader = new BufferedReader(new FileReader("Demand.csv"));
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                String Product_Code = parts[0];
                String Location = parts[1];
                int Demand_Qty = Integer.parseInt(parts[2]);

                // Parse date from string to java.sql.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date parsedDate = dateFormat.parse(parts[3]);
                java.sql.Date Demand_Date = new java.sql.Date(parsedDate.getTime());

                // Insert data into MySQL
                String sql = "INSERT INTO Demand (Product_Code, Location, Demand_Qty, Demand_Date) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,Product_Code);
                statement.setString(2, Location);
                statement.setInt(3, Demand_Qty);
                statement.setDate(4, Demand_Date);
                statement.executeUpdate();
            }

            reader.close();
            connection.close();

            System.out.println("Demand details loaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getInventoryLevel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product code: ");
        String Product_Code = scanner.next();

        try {
            Connection connection = getConnection();

            int totalSupply = getTotalSupply(connection, Product_Code);
            int totalDemand = getTotalDemand(connection, Product_Code);

            int InventoryLevel = totalSupply - totalDemand;

            System.out.println("Inventory Level for product " + Product_Code + " is: " + InventoryLevel);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getTotalSupply(Connection connection, String Product_Code) throws SQLException {
        String sql = "SELECT SUM(Supply_Qty) FROM Supply WHERE Product_Code = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Product_Code);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        int totalSupply = resultSet.getInt(1);

        resultSet.close();
        statement.close();

        return totalSupply;
    }

    private static int getTotalDemand(Connection connection, String Product_Code) throws SQLException {
        String sql = "SELECT SUM(Demand_Qty) FROM Demand WHERE Product_Code = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Product_Code);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        int totalDemand = resultSet.getInt(1);

        resultSet.close();
        statement.close();

        return totalDemand;
    }

    private static void generateCSV() {
        try {
            Connection connection = getConnection();

            String sql = "SELECT Supply.Product_Code, SUM(Supply_Qty) - SUM(Demand_Qty) AS inventory_level " +
            "FROM Supply INNER JOIN Demand ON Supply.Product_Code = Demand.Product_Code " +
            "GROUP BY Supply.Product_Code";


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            FileWriter writer = new FileWriter("InventoryLevel.csv");
            writer.append("Product_Code,Inventorylevel\n");

            while (resultSet.next()) {
                String Product_Code = resultSet.getString("Product_Code");
                int InventoryLevel = resultSet.getInt("inventory_level"); // Corrected column name
            
                writer.append(Product_Code + "," + InventoryLevel + "\n");
            }
            
            resultSet.close();
            statement.close();
            writer.close();

            System.out.println("Inventory levels written to InventoryLevel.csv.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
