package com.suretrust.inventroy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Supply {
    private int Product_code;
    private String Location;
    private int Quantity;
    private String Supply_date;

    public int getProduct_code() {
        return Product_code;
    }

    public void setProduct_code(int product_code) {
        Product_code = product_code;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getSupply_date() {
        return Supply_date;
    }

    public void setSupply_date(String supply_date) {
        Supply_date = supply_date;
    }
    public  static void addSupply(Supply supplyObj) throws SQLException {
        String INSERT_QUERY="INSERT INTO SUPPLY VALUES(?,?,?,?)";
        PreparedStatement psmt1 = ConnectionFactroy.preExecute(INSERT_QUERY);
        psmt1.setInt(1,supplyObj.getProduct_code());
        psmt1.setString(2,supplyObj.getLocation());
        psmt1.setInt(3,supplyObj.getQuantity());
        psmt1.setString(4,supplyObj.getSupply_date());
        psmt1.execute();
    }
    public  static void displyaSupply() throws SQLException {
        String DISPLAY_QUERY="SELECT * FROM SUPPLY";
        ConnectionFactroy.executeQueryy(DISPLAY_QUERY);
    }
    public  static int sumOfQuntitySupply(int productCode) throws SQLException {
        String query="SELECT SUM(SupplyQuantity) FROM supply WHERE product_code=?;";
        PreparedStatement psmt1= ConnectionFactroy.preExecute(query);
        psmt1.setInt(1,productCode);
        ResultSet resultSet=psmt1.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);

    }
}
