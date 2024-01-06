package com.suretrust.inventroy;

import java.sql.*;

public class ConnectionFactroy {
    static Connection con=null;
    static PreparedStatement psmt=null;
    ConnectionFactroy() throws SQLException {
        con= DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");
    }
    public  static Connection getConetion() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","root");

    }
    public static PreparedStatement preExecute(String query) throws SQLException {
        return getConetion().prepareStatement(query);

    }
    public  static void executeQueryy(String query) throws SQLException {
        ResultSet resultSet=preExecute(query).executeQuery(query);
        while (resultSet.next()){
            System.out.println("Product_code:"+resultSet.getInt(1)+" Location:"+resultSet.getString(2)+" Quantity:"+resultSet.getInt(3)+" date:"+resultSet.getString(4));
        }
        return;


    }
}
