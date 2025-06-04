# Company-Info-SQL-DBS
This is a SQL database I created for a class based in database creation, management, and SQL coding. The ER tables include information about customers, employees, products, and more. This Java project interfaces with a MySQL database to perform various customer and order-related operations, such as creating, updating, and deleting records, as well as transactional and reporting queries. There are both SQL and Java files included here. 

## Features

- **Step 5_1:** Create a new customer.
- **Step 5_2:** View all orders for a specific customer.
- **Step 5_3:** Update the price of a product.
- **Step 5_4:** Delete a customer by their ID.
- **Step 6_1:** Use joins to retrieve customer and product order data.
- **Step 6_2:** Calculate the total amount spent by a customer.
- **Step 6_3:** List customers who placed orders within a date range.
- **Step 7_1:** Create a transactional query to add a new order, update order lines, and adjust product quantities, with commit and rollback handling.

## Technologies

- Java
- MySQL
- MySQL Workbench

## Usage

Each program in the project prompts for specific input fields (e.g., customer details, product codes, dates) and updates the database accordingly. Upon success or failure, the program will display appropriate messages, including detailed error messages if constraints or queries fail.

## Error Handling

Programs include robust error handling for issues such as:
- Foreign key constraints
- Database connection errors
- Invalid input data types
- Transaction rollback in case of errors

## Screenshots & Documentation

For screenshots of each operation and detailed step-by-step instructions, refer to the `Screenshots & Read Me Files.pdf` file in this repository.

---
