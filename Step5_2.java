package com.myapp.database;  // declaring the database

import java.sql.*;     // Manages the database connection
import java.util.Scanner;

public class Step5_2 {                      // declaring the class (project file)
    public static void main(String[] args) {
        // Database credentials -- using my own information to establish the connection to the database 
        String jdbcURL = "jdbc:mysql://localhost:3306/final_project"; 
        String username = "root"; 
        String password = "melkor22"; 
        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Collecting the customer information from the user - the customer ID is used to search through all of the orders
        System.out.println("Enter the Customer ID: ");
        String customerID = scanner.nextLine();

        // SQL query to show all available orders for the customer that the user input
        String sql = "SELECT " +          // using Select First as part of the query
                     "Orders.orderID, Orders.orderedDate, Orders.orderStatus, Orders.shippedDate, " + // grabbing all of the necessary information
                     "Orders.shipperID, Orders.shipToName, Orders.shipToAddress, " +             // about this customer that we want to see on the screen
                     "Orders.shipToCity, Orders.shipToCountry, Orders.shipToPostalCode " +        // all of this information comes from the orders table
                     "FROM Orders " +                                                   // as noted above
                     "JOIN Customers ON Orders.customerID = Customers.customerID " +         // joining these fields with an inner join using the customers table to ensure we are
                     "WHERE Customers.customerID = '" + customerID + "'";               // finding the correct customer that all of these orders are for

        try {
            // Establishes the connection to the database 
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            // Creating the connection statement
            Statement statement = connection.createStatement();

            // Executing the query, running the program
            ResultSet resultSet = statement.executeQuery(sql);

            // Processing the result set using a while statement that processes all of the necessary string field information from the orders table
            // that we need to know about the customer
            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                Date orderedDate = resultSet.getDate("orderedDate");
                String orderStatus = resultSet.getString("orderStatus");
                Date shippedDate = resultSet.getDate("shippedDate");
                int shipperID = resultSet.getInt("shipperID");
                String shipToName = resultSet.getString("shipToName");
                String shipToAddress = resultSet.getString("shipToAddress");
                String shipToCity = resultSet.getString("shipToCity");
                String shipToCountry = resultSet.getString("shipToCountry");
                String shipToPostalCode = resultSet.getString("shipToPostalCode");

                // Showing the order details on the screen to the user 
                System.out.println("Order ID: " + orderID);
                System.out.println("Ordered Date: " + orderedDate);
                System.out.println("Order Status: " + orderStatus);
                System.out.println("Shipped Date: " + shippedDate);
                System.out.println("Shipper ID: " + shipperID);
                System.out.println("Ship To Name: " + shipToName);
                System.out.println("Ship To Address: " + shipToAddress);
                System.out.println("Ship To City: " + shipToCity);
                System.out.println("Ship To Country: " + shipToCountry);
                System.out.println("Ship To Postal Code: " + shipToPostalCode);
                System.out.println("-------------------------------------");
            }

            // Closing the result set, statement, and connection so that the program can end successfully 
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
