package SourceCode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FoodDatabaseManager {

    private static final String URL = "jdbc:your_database_url";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
      
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
           
            createFoodsTable(connection);

         
            insertSampleData(connection);

            insertFoodRequest(connection, 1, "John Doe", 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createFoodsTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Foods (" +
                "Food_ID INT PRIMARY KEY AUTO_INCREMENT," +
                "Food_Name VARCHAR(255) NOT NULL," +
                "Food_Type VARCHAR(50) NOT NULL," +
                "Shelf_Life_Hours INT," +
                "Packaging VARCHAR(3) CHECK (Packaging IN ('Yes', 'No'))" +
                ");";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void insertSampleData(Connection connection) throws SQLException {
        String insertDataSQL = "INSERT INTO Foods (Food_Name, Food_Type, Shelf_Life_Hours, Packaging) VALUES (?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
            
            insertFood(preparedStatement, "Cake", "Processed", 16, "No");
            insertFood(preparedStatement, "Rice", "Grains", 12, "No");
            insertFood(preparedStatement, "Bread", "Processed", 48, "Yes");
            insertFood(preparedStatement, "Chapathi", "Grains", 24, "No");
        }
    }

    private static void insertFood(PreparedStatement preparedStatement, String foodName, String foodType, int shelfLife, String packaging) throws SQLException {
        preparedStatement.setString(1, foodName);
        preparedStatement.setString(2, foodType);
        preparedStatement.setInt(3, shelfLife);
        preparedStatement.setString(4, packaging);
        preparedStatement.executeUpdate();
    }

    private static void insertFoodRequest(Connection connection, int foodId, String customerName, int requestedQuantity) throws SQLException {
        String insertRequestSQL = "INSERT INTO Food_Requests (Food_ID, Customer_Name, Requested_Quantity) VALUES (?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertRequestSQL)) {
            preparedStatement.setInt(1, foodId);
            preparedStatement.setString(2, customerName);
            preparedStatement.setInt(3, requestedQuantity);
            preparedStatement.executeUpdate();
        }
    }
}

