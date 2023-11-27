package com.suretrust.inventroy;

import java.sql.SQLException;
import java.util.Scanner;

class Main{
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);


        while (true){
            System.out.println("Enter 1 for Load Supply Details ");
            System.out.println("Enter 2 for Load Demand Details ");
            System.out.println("Enter 3 for Getting Inventory Level for One Product");
            System.out.println("Enter 4 for Generate Inventory Level CSV File");
            System.out.println("Enter 5 for displaying Supply Deatile");
            System.out.println("Enter 6 for displaying Demand Deatile");
            System.out.println("Enter 7 to exit");
            int num=input.nextInt();

            switch (num){
                case 1:
                    Supply sup1 = new Supply();
                    System.out.println("Enter the productCode:");
                    sup1.setProduct_code(input.nextInt());
                    System.out.println("Enter the Location:");
                    sup1.setLocation(input.next());
                    System.out.println("Enter the Quantity");
                    sup1.setQuantity(input.nextInt());
                    System.out.println("Enter the Date(yyyy-mm-dd)");
                    sup1.setSupply_date(input.next());
                    try {
                        Supply.addSupply(sup1);
                        System.out.println("Added the supply Deatiles");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    Demand Demand1 = new Demand();
                    System.out.println("Enter the productCode:");
                    Demand1.setProduct_code(input.nextInt());
                    System.out.println("Enter the Location:");
                    Demand1.setLocation(input.next());
                    System.out.println("Enter the Quantity");
                    Demand1.setDemand_Qty(input.nextInt());
                    System.out.println("Enter the Date(yyyy-mm-dd)");
                    Demand1.setDemand_Date(input.next());
                    try {
                        Demand.addDemand(Demand1);
                        System.out.println("Added the Demand Deatiles");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 5:
                    try {
                        Supply.displyaSupply();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case 6:
                    try {
                        Demand.displyaDemand();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                case 7:
                    System.exit(0);
                    break;
                case 3:
                    System.out.println("Enter the produc_code to calulate the Inventory Level:");
                    int productCode=input.nextInt();
                    try {
                        System.out.println("Inventroy Level for "+productCode+" :"+InventoryMethods.inventoryLevel(productCode));

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case 4:
                    try {
                        InventoryMethods.calculate_InventoryLevel_forAll_Items();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


            }
        }




    }
}
