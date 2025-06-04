package com.myapp.database; // declaring the database

import java.sql.Connection;  // Manages the database connection
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Step5_4 {             // declaring the class (project file)
    public static void main(String[] args) {
        // Database credentials -- using my own information to establish the connection to the database 
        String url = "jdbc:mysql://localhost:3306/Final_Project";
        String user = "root";
        String password = "melkor22";
        // Creating a scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Prompting the user for the customer for the customer they wish to delete
        System.out.println("Enter the Customer ID to delete: ");
        int customerID = Integer.parseInt(scanner.nextLine());

        // SQL queries - ensuring that all spaces where this customer existed and had ties to from any foreign keys are deleted
        String deleteOrderLinesSQL = "DELETE FROM OrderLines WHERE orderID IN (SELECT orderID FROM Orders WHERE customerID = ?)";
        String deleteInvoiceLinesSQL = "DELETE FROM InvoiceLines WHERE invoiceID IN (SELECT invoiceID FROM Invoices WHERE orderID IN (SELECT orderID FROM Orders WHERE customerID = ?))";
        String deleteInvoicesSQL = "DELETE FROM Invoices WHERE orderID IN (SELECT orderID FROM Orders WHERE customerID = ?)";
        String deleteOrdersSQL = "DELETE FROM Orders WHERE customerID = ?";
        String deleteCustomerSQL = "DELETE FROM Customers WHERE customerID = ?";

        // Establishes the connection to the database 
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false); // Starting transaction

            try {
                // Step 1: Deleting related order lines - from above SQL query, now actually executing program query
                try (PreparedStatement pstmtDeleteOrderLines = conn.prepareStatement(deleteOrderLinesSQL)) {
                    pstmtDeleteOrderLines.setInt(1, customerID);
                    int orderLinesDeleted = pstmtDeleteOrderLines.executeUpdate();
                    System.out.println("Deleted " + orderLinesDeleted + " related order line(s).");   // this line prints when anything was deleted, 0 if nothing was deleted
                }

                // Step 2: Delete related invoice lines - from above SQL query, now actually executing program query
                try (PreparedStatement pstmtDeleteInvoiceLines = conn.prepareStatement(deleteInvoiceLinesSQL)) {
                    pstmtDeleteInvoiceLines.setInt(1, customerID);
                    int invoiceLinesDeleted = pstmtDeleteInvoiceLines.executeUpdate();
                    System.out.println("Deleted " + invoiceLinesDeleted + " related invoice line(s).");
                }

                // Step 3: Delete related invoices - from above SQL query, now actually executing program query
                try (PreparedStatement pstmtDeleteInvoices = conn.prepareStatement(deleteInvoicesSQL)) {
                    pstmtDeleteInvoices.setInt(1, customerID);
                    int invoicesDeleted = pstmtDeleteInvoices.executeUpdate();
                    System.out.println("Deleted " + invoicesDeleted + " related invoice(s).");
                }

                // Step 4: Delete related orders - from above SQL query, now actually executing program query
                try (PreparedStatement pstmtDeleteOrders = conn.prepareStatement(deleteOrdersSQL)) {
                    pstmtDeleteOrders.setInt(1, customerID);
                    int ordersDeleted = pstmtDeleteOrders.executeUpdate();
                    System.out.println("Deleted " + ordersDeleted + " related order(s).");
                }

                // Step 5: Delete the customer - from above SQL query, now actually executing program query
                // last step - this is to actually delete the customer, but we had to do the steps above
                // in order to delete all related records as well and all foreign key constraints 
                try (PreparedStatement pstmtDeleteCustomer = conn.prepareStatement(deleteCustomerSQL)) {
                    pstmtDeleteCustomer.setInt(1, customerID);
                    int customersDeleted = pstmtDeleteCustomer.executeUpdate();
                    if (customersDeleted > 0) {
                        System.out.println("Customer deleted successfully.");   // will print upon successful deletion
                    } else {
                        System.out.println("No customer found with the provided Customer ID.");   // will print if no customer exists with that customer ID the user entered
                    }
                }

                conn.commit(); // Committing the transaction
                System.out.println("Transaction committed successfully.");      // will print if transaction completed 
            } catch (SQLException e) {
                conn.rollback(); // Rollback if any step fails
                System.err.println("Transaction rolled back due to an error.");    // will print if there was an error 
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}