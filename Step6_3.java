package com.myapp.database;            // declaring the database
 
import java.sql.*;                   // Manages the database connection
import java.util.Scanner;

public class Step6_3 {                 // declaring the class (project file)
    public static void main(String[] args) {
    	 // Database credentials -- using my own information to establish the connection to the data
        String jdbcURL = "jdbc:mysql://localhost:3306/Final_Project";
        String dbUser = "root"; 
        String dbPassword = "melkor22";

        // SQL query with placeholders for dates
        // using inner join to join data from the orders and customers tables
        // select statement shows what information we are looking for that will be shown on the screen
        // from the orders table, joining it with the information we want from the customers table
        // where statement to show that the information should be located within the two specified date ranges represented by question marks
        // and finally ordered by order date 
        String sqlQuery = """
                SELECT c.customerID, c.contactName, o.orderedDate
                FROM Orders o
                JOIN Customers c ON o.customerID = c.customerID
                WHERE o.orderedDate BETWEEN ? AND ?
                ORDER BY o.orderedDate;
                """;

        					// program is staring with prepare statement, beginning of program 
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            // Prompting the user to input both a start date and an end date for the date ranges 
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the start date (YYYY-MM-DD): ");
            String startDate = scanner.nextLine();
            System.out.print("Enter the end date (YYYY-MM-DD): ");
            String endDate = scanner.nextLine();

            // Setting the date parameters in the query from the user input
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);

            // Executing the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // printing the output results titles - printing the customer id, contact name, and order date with dashes to split the information visually - 
                System.out.printf("%-10s %-30s %-15s%n", "CustomerID", "ContactName", "OrderDate");
                System.out.println("-------------------------------------------------------------");
 
                // using while statement to retrieve specified information 
                boolean hasResults = false;
                while (resultSet.next()) {
                    int customerID = resultSet.getInt("customerID");
                    String contactName = resultSet.getString("contactName");
                    Date orderedDate = resultSet.getDate("orderedDate");
                  // printing the results
                    System.out.printf("%-10d %-30s %-15s%n", customerID, contactName, orderedDate);
                    hasResults = true;
                }

                if (!hasResults) {     // if there are no results with the input customer id
                    System.out.println("No customers found with orders in the specified date range."); // will print this error message if there is no information found between the specified dates that the user input
                }
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query failed!");      // will print this error message if the database connection or query failed
            e.printStackTrace();
        }
    }
}