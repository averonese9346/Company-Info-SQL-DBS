package com.myapp.database; // declaring the database

import java.sql.*;            // Manages the database connection
import java.util.Scanner;     

public class Step6_1 {            // declaring the class (project file)
    public static void main(String[] args) {
        // Database credentials -- using my own information to establish the connection to the database 
        final String URL = "jdbc:mysql://localhost:3306/Final_Project";
        final String USER = "root"; 
        final String PASSWORD = "melkor22"; 

        // SQL query using inner JOINs with a parameter for the customer ID
        // under the SELECT fields are the columns that we are taking from each table and assigning them to an alias 
        // the join lines match the corresponding columns from each table
        // Lastly there is a where statement that lets the user input any customer ID to retrieve this information 
        String query = """
                SELECT 
                    Customers.contactName AS CustomerName,                       
                    Customers.companyName AS CompanyName,
                    PProducts.name AS ProductName,
                    PProducts.description AS ProductDescription,
                    OrderLines.quantity AS Quantity,
                    OrderLines.unitPrice AS UnitPrice
                FROM 
                    Customers
                INNER JOIN Orders ON Customers.customerID = Orders.customerID
                INNER JOIN OrderLines ON Orders.orderID = OrderLines.orderID
                INNER JOIN PProducts ON OrderLines.productCode = PProducts.productCode
                WHERE 
                    Customers.customerID = ?;
                """;

        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Prompting the user to enter a customer ID
            System.out.print("Enter the Customer ID: ");
            int customerIDInput = Integer.parseInt(scanner.nextLine());

            // Setting the parameter for the customer ID
            preparedStatement.setInt(1, customerIDInput);
           // executing the query 
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\nCustomer and Purchased Product Details:");
                System.out.printf("%-20s %-30s %-25s %-50s %-10s %-10s\n",
                        "Customer Name", "Company Name", "Product Name", "Product Description", "Quantity", "Unit Price");

                boolean dataFound = false;
                // this is the information that the query is finding about the customer and their products, the result set
                while (resultSet.next()) {
                    dataFound = true;
                    String customerName = resultSet.getString("CustomerName");
                    String companyName = resultSet.getString("CompanyName");
                    String productName = resultSet.getString("ProductName");
                    String productDescription = resultSet.getString("ProductDescription");
                    int quantity = resultSet.getInt("Quantity");
                    double unitPrice = resultSet.getDouble("UnitPrice");
                      // printing results - print statement 
                    System.out.printf("%-20s %-30s %-25s %-50s %-10d $%-10.2f\n",
                            customerName, companyName, productName, productDescription, quantity, unitPrice);
                }

                if (!dataFound) {      // prints this error message if there is no customer with that associated customer ID the user input
                    System.out.println("No records found for the Customer ID: " + customerIDInput);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());         // prints this error for a database error
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a valid numeric Customer ID.");       // prints this error if the user does not input a numeric valid input
        }
    }
}
