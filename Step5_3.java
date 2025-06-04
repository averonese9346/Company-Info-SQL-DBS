package com.myapp.database;   // declaring the database

import java.sql.*;     // Manages the database connection
import java.util.Scanner;

public class Step5_3 {         // declaring the class (project file)
    public static void main(String[] args) {
        // Database credentials -- using my own information to establish the connection to the database
        String url = "jdbc:mysql://localhost:3306/Final_Project";
        String username = "root";
        String password = "melkor22";

        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Collecting the product code information that the user wants to update
        System.out.println("Enter the product code: ");
        String productCode = scanner.nextLine();

        // Collecting the new price of the item that the user input
        System.out.println("Enter the new price for the item: ");
        double suggestedUnitPrice = scanner.nextDouble();  // Changed to double to store the price as a number

        // SQL query to update the price of an item with the given product code from the user
        String sql = "UPDATE PProducts SET suggestedUnitPrice = ? WHERE productCode = ?";

        // Establishing a connection and executing the update query
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting the values for the prepared statement (index 1 for the price, index 2 for the product code)
            pstmt.setDouble(1, suggestedUnitPrice);  // Set the price parameter
            pstmt.setString(2, productCode);  // Set the product code parameter

            // Executing the update query
            int rowsAffected = pstmt.executeUpdate();

            // Checking if the update was successful
            if (rowsAffected > 0) {
                System.out.println("Price updated successfully!");  // will print for correct update
            } else {
                System.out.println("No product found with the given product code.");   // will print for error with incorrect product code
            }
        } catch (SQLException e) {
            System.out.println("Error updating the product price: " + e.getMessage());    // will print for error with product price
        }

        // Close the scanner resource, ending the program
        scanner.close();
    }
}