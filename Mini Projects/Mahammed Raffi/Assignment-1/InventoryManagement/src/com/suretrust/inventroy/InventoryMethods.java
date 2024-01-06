package com.suretrust.inventroy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class InventoryMethods {
    public static int inventoryLevel(int productCode) throws SQLException {
        return Supply.sumOfQuntitySupply(productCode) - Demand.sumOfQuntityDemand(productCode);
    }
    public static void calculate_InventoryLevel_forAll_Items() throws SQLException {
        String Query="SELECT * FROM PRODUCT";
        ResultSet resultSet=ConnectionFactroy.preExecute(Query).executeQuery(Query);
        while (resultSet.next()){
            System.out.println("Inventroy Level for "+resultSet.getInt(1)+":"+inventoryLevel(resultSet.getInt(1)));
        }
    }


}

