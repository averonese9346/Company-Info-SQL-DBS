package com.myapp.database;         // declaring the database

import java.sql.*;               // Manages the database connection
import java.util.Scanner;

public class Step6_2 {              // declaring the class (project file)

    public static void main(String[] args) {
        // Database credentials -- using my own information to establish the connection to the database 
        String jdbcURL = "jdbc:mysql://localhost:3306/Final_Project"; 
        String dbUser = "root";
        String dbPassword = "melkor22"; 

        // SQL query to calculate the total amount spent by a customer
        // using SELECT SUM to calculate the information from the payments table and amounts as total spent
        // using inner join to join the orders and order ID with payments and customerID from order table
        // using where statement to include that this information should be applied to the customer ID that the user inputs 
        String sqlQuery = """
                SELECT SUM(Payments.amount) AS totalSpent 
                FROM Payments
                INNER JOIN Orders ON Payments.orderID = Orders.orderID
                WHERE Orders.customerID = ?;
                """;

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);    // Entry point of the program, program is beginning 
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {       // prepare statement

            // Prompting the user to enter a customer ID
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the Customer ID to retrieve total spending: ");
            int customerID = scanner.nextInt();

            // Setting the customer ID parameter in the query to look for this specific customer ID
            statement.setInt(1, customerID);

            // Executing the query, finding results
            ResultSet resultSet = statement.executeQuery();

            // Processing the result set
            if (resultSet.next()) {
                double totalAmount = resultSet.getDouble("totalSpent");       // processing result set
                if (resultSet.wasNull()) {
                    System.out.println("No spending records found for the provided Customer ID.");  // will show this error if the customer has not purchased any products
                } else {
                    System.out.println("Total amount spent by Customer ID " + customerID + ": $" + totalAmount);      // success statement
                }
            } else {
                System.out.println("No data found for the provided Customer ID.");       // will show this error if there was an error with the input customer ID
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());                // will show this error if there was an error with the database system
            e.printStackTrace();
        }
    }
}

