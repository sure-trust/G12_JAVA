package com.inventory;

import java.sql.*;
import java.io.*;
import java.util.Scanner;

public  class InventoryManagementSystem {
    private Connection connection;

    public  InventoryManagementSystem() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "http://localhost/phpmyadmin/index.php?route=/database/structure&db=Suretrust";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadSupplyDetailsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String productCode = data[0];
                    String location = data[1];
                    String supplyQuantity = data[2];
                    String supplyDate = data[3];
                    
                    try {
                        int parsedProductCode = Integer.parseInt(productCode);
                        int parsedSupplyQuantity = Integer.parseInt(supplyQuantity);
                        String sql = "INSERT INTO supply_details (product_code, location, supply_quantity, supply_date) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setInt(1, parsedProductCode);
                            preparedStatement.setString(2, location);
                            preparedStatement.setInt(3, parsedSupplyQuantity);
                            preparedStatement.setString(4, supplyDate);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid numeric value in the CSV: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid CSV format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void loadDemandDetailsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String productCode = data[0];
                    String location = data[1];
                    String demandQuantity = data[2];
                    String demandDate = data[3];
    
                    try {
                        int parsedProductCode = Integer.parseInt(productCode);
                        int parsedDemandQuantity = Integer.parseInt(demandQuantity);
                        String sql = "INSERT INTO demand_details (product_code, location, demand_quantity, demand_date) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setInt(1, parsedProductCode);
                            preparedStatement.setString(2, location);
                            preparedStatement.setInt(3, parsedDemandQuantity);
                            preparedStatement.setString(4, demandDate);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid numeric value in the CSV: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid CSV format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

public int calculateInventoryLevel(String productCode) {
        int totalSupply = 0;
        int totalDemand = 0;

        try (PreparedStatement supplyStatement = connection.prepareStatement("SELECT SUM(supply_quantity) FROM supply_details WHERE product_code = ?")) {
            supplyStatement.setString(1, productCode);
            ResultSet supplyResultSet = supplyStatement.executeQuery();
            if (supplyResultSet.next()) {
                totalSupply = supplyResultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement demandStatement = connection.prepareStatement("SELECT SUM(demand_quantity) FROM demand_details WHERE product_code = ?")) {
            demandStatement.setString(1, productCode);
            ResultSet demandResultSet = demandStatement.executeQuery();
            if (demandResultSet.next()) {
                totalDemand = demandResultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalSupply - totalDemand;
    }
    
    public void generateInventoryLevelCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT product_code, SUM(supply_quantity) - SUM(demand_quantity) AS inventory_level " +
                             "FROM supply_details " +
                             "LEFT JOIN demand_details ON supply_details.product_code = demand_details.product_code " +
                             "GROUP BY supply_details.product_code")) {
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                int productCode = resultSet.getInt("product_code");
                int inventoryLevel = resultSet.getInt("inventory_level");
                writer.write(productCode + "," + inventoryLevel + "\n");
            }
    
            System.out.println("Inventory level CSV generated successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        InventoryManagementSystem inventorySystem = new InventoryManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 for Supply Details Load from CSV File");
            System.out.println("Enter 2 for Demand Details Load from CSV File");
            System.out.println("Enter 3 for Getting Inventory Level for One Product");
            System.out.println("Enter 4 for Generate Inventory Level CSV File");
            System.out.println("Enter 5 to Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    String supplyFilePath = "/home/student/Desktop/InventoryManagemenet/app/data/supply_details.csv";
                    inventorySystem.loadSupplyDetailsFromCSV(supplyFilePath);
                    System.out.println("Supply details loaded successfully.");
                    break;
                case 2:
                    
                    String demandFilePath = "/home/student/Desktop/InventoryManagemenet/app/data/demand_details.csv";
                    inventorySystem.loadDemandDetailsFromCSV(demandFilePath);
                    System.out.println("Demand details loaded successfully.");
                    break;
                case 3:
                    System.out.println("Enter the product code:");
                    String productCode = scanner.nextLine();
                    int inventoryLevel = inventorySystem.calculateInventoryLevel(productCode);
                    System.out.println("Inventory Level for Product " + productCode + ": " + inventoryLevel);
                    break;
                case 4:
                    System.out.println("Enter the file path to save the inventory level CSV:");
                    String inventoryLevelFilePath = scanner.nextLine();
                    inventorySystem.generateInventoryLevelCSV(inventoryLevelFilePath);
                    break;
                case 5:
                    System.out.println("Exiting program. Goodbye!");
                    try {
                        if (inventorySystem.connection != null && !inventorySystem.connection.isClosed()) {
                            inventorySystem.connection.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
