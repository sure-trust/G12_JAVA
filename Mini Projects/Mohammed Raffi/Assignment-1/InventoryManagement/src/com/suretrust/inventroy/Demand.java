package com.suretrust.inventroy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Demand {
    private int Product_code;
    private String Location;
    private int demand_Qty;
    private String demand_Date;

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

    public int getDemand_Qty() {
        return demand_Qty;
    }

    public void setDemand_Qty(int demand_Qty) {
        this.demand_Qty = demand_Qty;
    }

    public String getDemand_Date() {
        return demand_Date;
    }

    public void setDemand_Date(String demand_Date) {
        this.demand_Date = demand_Date;
    }
    public  static void addDemand(Demand demandObj) throws SQLException {
        String INSERT_QUERY="INSERT INTO Demand VALUES(?,?,?,?)";
        PreparedStatement psmt1 = ConnectionFactroy.preExecute(INSERT_QUERY);
        psmt1.setInt(1,demandObj.getProduct_code());
        psmt1.setString(2,demandObj.getLocation());
        psmt1.setInt(3,demandObj.getDemand_Qty());
        psmt1.setString(4,demandObj.getDemand_Date());
        psmt1.execute();
    }
    public  static void displyaDemand() throws SQLException {
        String DISPLAY_QUERY="SELECT * FROM Demand";
        ConnectionFactroy.executeQueryy(DISPLAY_QUERY);
    }
    public  static int sumOfQuntityDemand(int productCode) throws SQLException {
        String query="SELECT SUM(Demand_Qty) FROM demand WHERE product_code=?";
        PreparedStatement psmt1= ConnectionFactroy.preExecute(query);
        psmt1.setInt(1,productCode);
        ResultSet resultSet=psmt1.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);

    }
}
