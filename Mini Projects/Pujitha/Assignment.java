package puji_first;

public class Assignment2 {

	public static void main(String[] args) {
		import java.sql.*;
		import java.util.*;
		import java.io.*;

		public class InventoryManagementSystem {
		    static final String JDBC_URL = "jdbc:mysql://localhost:3306/inventorymanagementsystem";
		    static final String USERNAME = "InventoryManagementSystem";
		    static final String PASSWORD = "password";

		    static Connection conn = null;
		    static Statement stmt = null;

		    public static void main(String[] args) {
		        try {
		            Class.forName("com.mysql.cj.jdbc.Driver");
		            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		            stmt = conn.createStatement();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		        Scanner scanner = new Scanner(System.in);

		        while (true) {
		            System.out.println("Enter 1 for Supply Details Load from CSV File");
		            System.out.println("Enter 2 for Demand Details Load from CSV File");
		            System.out.println("Enter 3 for Getting Inventory Level for One Product");
		            System.out.println("Enter 4 for Generate Inventory Level CSV File");
		            System.out.println("Enter 5 to Exit");

		            int choice = scanner.nextInt();
		            scanner.nextLine(); // Consume newline

		            switch (choice) {
		                case 1:
		                    loadSupplyDetailsFromCSV();
		                    break;
		                case 2:
		                    loadDemandDetailsFromCSV();
		                    break;
		                case 3:
		                    System.out.print("Enter Product Code: ");
		                    String productCode = scanner.nextLine();
		                    calculateInventoryLevel(productCode);
		                    break;
		                case 4:
		                    generateInventoryLevelCSV();
		                    break;
		                case 5:
		                    try {
		                        if (stmt != null)
		                            stmt.close();
		                        if (conn != null)
		                            conn.close();
		                    } catch (SQLException se) {
		                        se.printStackTrace();
		                    }
		                    System.exit(0);
		                default:
		                    System.out.println("Invalid choice. Please try again.");
		            }
		        }
		    }

		    static void loadSupplyDetailsFromCSV() {
		        try {
		            BufferedReader reader = new BufferedReader(new FileReader("Supply.csv"));
		            String line;
		            while ((line = reader.readLine()) != null) {
		                String[] parts = line.split(",");
		                String productCode = parts[0];
		                String location = parts[1];
		                int supplyQuantity = Integer.parseInt(parts[2]);
		                String supplyDate = parts[3];

		                String sql = "INSERT INTO Supply (Product_code, Location, Supply_quantity, Supply_date) " +
		                             "VALUES ('" + productCode + "', '" + location + "', " + supplyQuantity + ", '" + supplyDate + "')";

		                stmt.executeUpdate(sql);
		            }
		            System.out.println("Supply details loaded successfully.");
		        } catch (IOException | SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    static void loadDemandDetailsFromCSV() {
		        try {
		            BufferedReader reader = new BufferedReader(new FileReader("Demand.csv"));
		            String line;
		            while ((line = reader.readLine()) != null) {
		                String[] parts = line.split(",");
		                String productCode = parts[0];
		                String location = parts[1];
		                int demandQty = Integer.parseInt(parts[2]);
		                String demandDate = parts[3];

		                String sql = "INSERT INTO Demand (Product_code, Location, Demand_Qty, Demand_date) " +
		                             "VALUES ('" + productCode + "', '" + location + "', " + demandQty + ", '" + demandDate + "')";

		                stmt.executeUpdate(sql);
		            }
		            System.out.println("Demand details loaded successfully.");
		        } catch (IOException | SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    static void calculateInventoryLevel(String productCode) {
		        try {
		            String sql = "SELECT SUM(Supply_Quantity) AS TotalSupply FROM Supply WHERE Product_code = '" + productCode + "'";
		            ResultSet supplyResult = stmt.executeQuery(sql);
		            supplyResult.next();
		            int totalSupply = supplyResult.getInt("TotalSupply");

		            sql = "SELECT SUM(Demand_Qty) AS TotalDemand FROM Demand WHERE Product_code = '" + productCode + "'";
		            ResultSet demandResult = stmt.executeQuery(sql);
		            demandResult.next();
		            int totalDemand = demandResult.getInt("TotalDemand");

		            int inventoryLevel = totalSupply - totalDemand;

		            System.out.println("Inventory Level for " + productCode + ": " + inventoryLevel);
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    static void generateInventoryLevelCSV() {
		        try {
		            String sql = "SELECT Product_code, (SUM(Supply_Quantity) - SUM(Demand_Qty)) AS Inventory_Level " +
		                         "FROM Supply INNER JOIN Demand ON Supply.Product_code = Demand.Product_code " +
		                         "GROUP BY Supply.Product_code";

		            ResultSet result = stmt.executeQuery(sql);

		            PrintWriter writer = new PrintWriter("InventoryLevel.csv");
		            writer.println("Product_code,inventory_level");

		            while (result.next()) {
		                String productCode = result.getString("Product_code");
		                int inventoryLevel = result.getInt("Inventory_Level");
		                writer.println(productCode + "," + inventoryLevel);
		            }

		            writer.close();
		            System.out.println("Inventory Level CSV generated successfully.");
		        } catch (SQLException | FileNotFoundException e) {
		            e.printStackTrace();
		        }
		    }
		}
	}

}
