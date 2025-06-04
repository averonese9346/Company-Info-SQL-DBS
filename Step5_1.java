package com.myapp.database; // declaring the database

import java.sql.Connection;           // Manages the database connection
import java.sql.DriverManager;        // Establishes the connection with the database
import java.sql.PreparedStatement;    // Prepares SQL queries with parameters
import java.sql.SQLException;         // Manages SQL-related exceptions
import java.util.Scanner;             // Handles user input

public class Step5_1 {               // declaring the class (project file)

    // Database credentials -- using my own information to establish the connection to the database 
    private static final String URL = "jdbc:mysql://localhost:3306/final_project";    
    private static final String USER = "root";
    private static final String PASSWORD = "melkor22";

    // Establishing the connection to the correct table from the database 
    private static final String TABLE_NAME = "customers";  // This is the table name from the database that we are working with in this program

    public static void main(String[] args) { // Entry point of the program, program is beginning 
        Scanner scanner = new Scanner(System.in);

        // Collecting the customer information from the user - all fields from the 'customer' table in the database
        System.out.println("Enter contact name: ");
        String contactName = scanner.nextLine();

        System.out.println("Enter company name: ");
        String companyName = scanner.nextLine();

        System.out.println("Enter job title: ");
        String contactJobTitle = scanner.nextLine();

        System.out.println("Enter office phone number: ");
        String officePhone = scanner.nextLine();

        System.out.println("Enter mobile phone number: ");
        String mobilePhone = scanner.nextLine();

        System.out.println("Enter fax number: ");
        String fax = scanner.nextLine();

        System.out.println("Enter email address: ");
        String email = scanner.nextLine();

        System.out.println("Enter address: ");
        String address = scanner.nextLine();

        System.out.println("Enter city: ");
        String city = scanner.nextLine();

        System.out.println("Enter state: ");
        String state = scanner.nextLine();

        System.out.println("Enter postal code: ");
        String postalCode = scanner.nextLine();

        System.out.println("Enter sales representative ID: ");
        String salesRepID = scanner.nextLine();

        System.out.println("Enter credit limit: ");
        String creditLimit = scanner.nextLine();

        System.out.println("Enter homepage URL: ");
        String homePage = scanner.nextLine();

        // Calling the method to insert data, so calling the main program to be able to implement the new data into the system
        insertCustomer(contactName, companyName, contactJobTitle, officePhone, mobilePhone, fax, email, address, city, state, postalCode, salesRepID, creditLimit, homePage);

        scanner.close();
    }

   // the part of the program that actually inputs the data into the database fields 
    public static void insertCustomer(String contactName, String companyName, String contactJobTitle, 
                                      String officePhone, String mobilePhone, String fax, String email, 
                                      String address, String city, String state, String postalCode, 
                                      String salesRepID, String creditLimit, String homePage) {
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (contactName, companyName, contactJobTitle, officePhone, " +
                             "mobilePhone, fax, email, address, city, state, postalCode, salesRepID, creditLimit, homePage) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Setting placeholder values 
                preparedStatement.setString(1, contactName);
                preparedStatement.setString(2, companyName);
                preparedStatement.setString(3, contactJobTitle);
                preparedStatement.setString(4, officePhone);
                preparedStatement.setString(5, mobilePhone);
                preparedStatement.setString(6, fax);
                preparedStatement.setString(7, email);
                preparedStatement.setString(8, address);
                preparedStatement.setString(9, city);
                preparedStatement.setString(10, state);
                preparedStatement.setString(11, postalCode);
                preparedStatement.setString(12, salesRepID);
                preparedStatement.setString(13, creditLimit);
                preparedStatement.setString(14, homePage);

                // Inserting data and checking data fields
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("New customer added successfully!");
                } else {
                    System.out.println("Failed to add customer.");
                }
            } catch (SQLException e) {
                // Printing detailed error information on the screen for the user in case an error occurs 
                e.printStackTrace();
                System.out.println("SQL Error: " + e.getMessage());
                if (e.getErrorCode() == 1452) { // This is the error code for foreign key constraint violation, foreign key here relates to the sales rep id from employee table
                    System.out.println("Foreign key constraint failed: The salesRepID doesn't exist.");
                }
            }
        } catch (SQLException e) {
            // If there is a connection error between this program and the database, the program will print this message 
            e.printStackTrace();
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
}