package com.myapp.database;           // declaring the database

import java.sql.*;                  // Manages the database connection
import java.util.InputMismatchException;
import java.util.Scanner;

public class Step7_1 {                   // declaring the class (project file)

    public static void main(String[] args) {
        // Database credentials -- using my own information to establish the connection to the database 
        String jdbcURL = "jdbc:mysql://localhost:3306/Final_Project"; 
        String dbUser = "root";
        String dbPassword = "melkor22"; 

        // Creating a scanner to read the user inputs
        Scanner scanner = new Scanner(System.in);

        // Starting the transaction
        String transactionStart = "START TRANSACTION;";

        // SQL query to insert into the Orders table
        // values from the user are represented by question marks
        String insertOrderSQL = """
            INSERT INTO Orders (customerID, employeeID, orderedDate, orderStatus, shippedDate, shipperID, shipToName, shipToAddress, shipToCity, shipToCountry, shipToPostalCode)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        // SQL query to insert into the OrderLines table
        // values from the user are represented by question marks
        String insertOrderLinesSQL = """
            INSERT INTO OrderLines (orderID, productCode, quantity, unitPrice, lineNumber)
            VALUES (?, ?, ?, ?, ?);
        """;

        // SQL query to update the PProducts table (update stock), also input by user 
        // values from the user are represented by question marks
        String updateProductStockSQL = """
            UPDATE PProducts 
            SET unitsInStock = ? 
            WHERE productCode = ?;
        """;

        // SQL to commit the transaction if all is successful 
        String commitSQL = "COMMIT;";

        // SQL to rollback the transaction if an error occurs 
        String rollbackSQL = "ROLLBACK;";

        // Try-with-resources for auto-closing resources (need to be able to close resources) 
        Connection connection = null;
        PreparedStatement insertOrderStmt = null;
        PreparedStatement insertOrderLinesStmt = null;
        PreparedStatement updateProductStmt = null;
        
        try {
            // Establishing connection to database 
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            connection.setAutoCommit(false); // Disable auto-commit mode because we are using rollbacks and manual commit 

            // Prepare SQL statements
            insertOrderStmt = connection.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            insertOrderLinesStmt = connection.prepareStatement(insertOrderLinesSQL);
            updateProductStmt = connection.prepareStatement(updateProductStockSQL);

            // Prompting the user to input customer ID
            int customerID = getIntInput(scanner, "Enter Customer ID:");

            // Prompting the user to enter Employee ID
            int employeeID = getIntInput(scanner, "Enter Employee ID:");

            // Prompting the user to enter the order date
            System.out.println("Enter ordered date (YYYY-MM-DD):");
            String orderedDate = scanner.nextLine(); // Use nextLine() to capture the entire input, was erroring out and sending several different lines
            // at the same time so I included this to ensure that only one line would populate at a time for the user to input 

            // Prompting the user to enter the order status 
            System.out.println("Enter order status:");
            String orderStatus = scanner.nextLine(); // Use nextLine() to capture the entire input

            // Prompting the user to enter shipped date
            System.out.println("Enter shipped date (YYYY-MM-DD):");
            String shippedDate = scanner.nextLine(); // Use nextLine() to capture the entire input

            // Prompting the user to enter shipper ID
            int shipperID = getIntInput(scanner, "Enter shipper ID:");

            // Prompting the user to enter all of the shipping details
            System.out.println("Enter ship to name:");
            String shipToName = scanner.nextLine();
            System.out.println("Enter ship to address:");
            String shipToAddress = scanner.nextLine();
            System.out.println("Enter ship to city:");
            String shipToCity = scanner.nextLine();
            System.out.println("Enter ship to country:");
            String shipToCountry = scanner.nextLine();
            int shipToPostalCode = getIntInput(scanner, "Enter ship to postal code:");

            // Setting parameters for the Orders table
            insertOrderStmt.setInt(1, customerID);
            insertOrderStmt.setInt(2, employeeID);
            insertOrderStmt.setString(3, orderedDate);
            insertOrderStmt.setString(4, orderStatus);
            insertOrderStmt.setString(5, shippedDate);
            insertOrderStmt.setInt(6, shipperID);
            insertOrderStmt.setString(7, shipToName);
            insertOrderStmt.setString(8, shipToAddress);
            insertOrderStmt.setString(9, shipToCity);
            insertOrderStmt.setString(10, shipToCountry);
            insertOrderStmt.setInt(11, shipToPostalCode);

            // Executing the insert into Orders table
            insertOrderStmt.executeUpdate();

            // Finding order ID (auto-generated) from the Orders table
            ResultSet generatedKeys = insertOrderStmt.getGeneratedKeys();
            int orderID = -1;
            if (generatedKeys.next()) {
                orderID = generatedKeys.getInt(1);
            }

            // Prompting the user to input information for OrderLines table
            System.out.println("Enter productCode:");
            String productCode = scanner.nextLine();
            int quantity = getIntInput(scanner, "Enter quantity:");
            double unitPrice = getDoubleInput(scanner, "Enter unit price:");
            int lineNumber = getIntInput(scanner, "Enter line number:");

            // Setting parameters for the OrderLines table
            insertOrderLinesStmt.setInt(1, orderID);  
            insertOrderLinesStmt.setString(2, productCode);
            insertOrderLinesStmt.setInt(3, quantity);
            insertOrderLinesStmt.setDouble(4, unitPrice);
            insertOrderLinesStmt.setInt(5, lineNumber);

            // Executing the insert into OrderLines table
            insertOrderLinesStmt.executeUpdate();

            // Prompting the user to enter new quantity information for products that were purchased 
            int newInventoryQuantity = getIntInput(scanner, "Enter new inventory quantity for product " + productCode + ":");

            // Setting parameters for updating product inventory
            updateProductStmt.setInt(1, newInventoryQuantity);  // Set the new quantity
            updateProductStmt.setString(2, productCode);

            // Executing the update on PProducts
            updateProductStmt.executeUpdate();

            // Committing the transaction
            connection.commit();
            System.out.println("Transaction committed successfully!");   // this message will print if all transactions are successful 

        } catch (SQLException e) {
            System.err.println("Error during transaction, rolling back changes...");        // this message will print if there was an error during the transaction and changes will be rolled back
            e.printStackTrace();

            // Rollback the transaction if there's an error
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Transaction rolled back.");         // this will print when a transaction is rolled back
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());        // this will print if there is an error during the rollback process
            }
        } finally {
            // Cleaning up resources
            try {
                if (insertOrderStmt != null) {
                    insertOrderStmt.close();
                }
                if (insertOrderLinesStmt != null) {
                    insertOrderLinesStmt.close();
                }
                if (updateProductStmt != null) {
                    updateProductStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());          // this message will print if there is an error closing resources
            }
        }
    }

    // Helper method for getting a valid integer input from the user
    private static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                return input;
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid integer.");     // this message will print if there is an error with the integers the user input
                scanner.nextLine(); // Consume invalid input 
            }
        }
    }

    // Helper method for getting a valid double input from the user 
    private static double getDoubleInput(Scanner scanner, String prompt) {         
        while (true) {
            try {
                System.out.println(prompt);
                double input = scanner.nextDouble();
                scanner.nextLine(); // Consume newline character
                return input;
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid number.");         // this message will print if there is an error with the numbers the user is inputting
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
}


