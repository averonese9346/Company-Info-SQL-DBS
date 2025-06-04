-- Creating the database
CREATE DATABASE IF NOT EXISTS Final_Project;

-- Making this the default database
USE Final_Project;

-- Creating first table, Employees
CREATE TABLE IF NOT EXISTS Employees (
    employeeID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,   -- auto incrementing employeeIDs. primary key cannot be null, it is an integer 
    lastName VARCHAR(70) NOT NULL DEFAULT '', -- setting all strings to default empty strings with '' and setting their character limits, ensuring all required fields are set to 'not null'
    firstName VARCHAR(70) NOT NULL DEFAULT '',
    jobTitle VARCHAR(30) NOT NULL DEFAULT '',
    reportsTo INT,                        -- this field left as 'int' because the employee id that each person reports to will be an integer 
    OfficePhone CHAR(10) UNIQUE,           -- this should be a unique field as no one else should have the same phone number
    mobilePhone CHAR(10) UNIQUE NOT NULL DEFAULT '',   -- this should be a unique field as no one else should have the same phone number
    email CHAR(60) UNIQUE NOT NULL DEFAULT '',      -- this should be a unique field as no one else should have the same email
    address VARCHAR(60) NOT NULL DEFAULT '',
    city VARCHAR(60) NOT NULL DEFAULT '',
    state VARCHAR(20) NOT NULL DEFAULT '',
    country VARCHAR(40) NOT NULL DEFAULT '',
    postalCode INT NOT NULL DEFAULT 0,    -- set to an integer as postal codes are integers and the default is 0
    homePhone VARCHAR(10) UNIQUE,
    photo BLOB,        -- photos are blob file types
    FOREIGN KEY (reportsTo) REFERENCES Employees(employeeID) ON DELETE SET NULL         -- setting the foreign keys at the end, the report to column which references employeeids, on delete set null to create a null value when deleted 
) AUTO_INCREMENT=200;

-- First we have to insert the employee that others report to as the 'parent employee' so that they can all report to this one person. it will error out if they are all added together 
INSERT INTO Employees (lastName, firstName, jobTitle, reportsTo, OfficePhone, mobilePhone, email, address, city, state, country, postalCode, homePhone, photo)
VALUES
    ('Smith', 'John', 'Software Engineer', NULL, '5551234568', '5559874564', 'john.smith@example.com', '1234 Elm Street', 'Springfield', 'IL', 'USA', 62701, '5552361254', 'F:\Downloads\John_Smith.jpg');

-- Inserting employees who report to employee 201
INSERT INTO Employees (lastName, firstName, jobTitle, reportsTo, OfficePhone, mobilePhone, email, address, city, state, country, postalCode, homePhone, photo)
VALUES
    ('Johnson', 'Emily', 'Project Manager', 201, '5552361452', '5554657894', 'emily.jonson12@example.lcom', '5678 Oak Avenue', 'Chicago', 'IL', 'USA', 60601, NULL, 'F:\Downloads\Emily_Johnson.jpg'),
    ('Williams', 'David', 'Data Analyst', 201, '5559874654', '5552369866', 'david.williams8@example.com', '910 Pine Road', 'Peoria', 'IL', 'USA', 61602, NULL, 'F:\Downloads\David_Williams.jpg'),
    ('Taylor', 'Sofia', 'HR Specialist', 201, '5551234877', '5551234555', 'sophia_taylor4@example.com', '345 Maple Lane', 'Bloomington', 'IN', 'USA', 47401, '5551237777', 'F:\Downloads\Alice_White.jpg'),
    ('Brown', 'Michael', 'Product Manager', 201, '5551284569', '5551211454', 'michaelbrown87@example.com', '234 Birch Avenue', 'Normal', 'IL', 'USA', '61761', NULL, 'F:\Downloads\Michael_Brown.jpg');
    
-- Checking to make sure data is imported correctly 
SELECT * FROM Employees

-- Creating second table, customers
CREATE TABLE IF NOT EXISTS Customers(
	customerID INT PRIMARY KEY NOT NULL AUTO_INCREMENT UNIQUE,   -- set to auto increment customerid, should be a completely unique value to each customer, setting required fields to 'not null', primary key
    companyName VARCHAR(50) NOT NULL DEFAULT '',   -- setting all character limits and default strings to ''
    contactName VARCHAR(50) NOT NULL DEFAULT '',
    contactJobTitle VARCHAR (30) NOT NULL DEFAULT '',
    officePhone CHAR(10) UNIQUE,            -- this should be a unique field as no one else should have the same phone number
    mobilePhone CHAR(10) UNIQUE NOT NULL DEFAULT '',        -- this should be a unique field as no one else should have the same phone number
    fax CHAR(10) UNIQUE,                           -- not required because faxes are outdated 
    email VARCHAR(60) UNIQUE NOT NULL DEFAULT '',          -- this should be a unique field as no one else should have the same email
    address VARCHAR(60) NOT NULL DEFAULT '',
    city VARCHAR(60) NOT NULL DEFAULT '',
    state VARCHAR(20) NOT NULL DEFAULT '',
    postalCode VARCHAR(10) NOT NULL DEFAULT 00000, -- default is set to 0 for postcal codes
    salesRepID INT NOT NULL,                     
    FOREIGN KEY (salesRepID) REFERENCES Employees (employeeID),       -- setting foreign keys which reference employee table
    creditLimit DECIMAL(10, 2) DEFAULT 0.00 NOT NULL CHECK (creditLimit <= 400000),       -- setting credit limit checks for the credit limits for each customer 
    homePage VARCHAR(70) UNIQUE NOT NULL            --
) AUTO_INCREMENT=100; 

-- Inserting data into the customers table
INSERT INTO Customers (companyName, contactName, contactJobTitle, officePhone, mobilePhone, fax, email, address, city, state, postalCode, salesRepID, creditLimit, homePage) VALUES
	('Apex Solutions, Inc.', 'Emily Johnson', 'Chief Technology Officer', '5551234567', '5554239685', '5553217645', 'emily.j@apexsolutions.com', '123 Innovation Blvd', 'Phoenix', 'AZ', 85001, 204, 5000.00, 'www.apexsolutions.com'),
    ('Orion Tech Co.', 'Michael Lee', 'Sales Director', '5556429785', '5554124569', NULL, 'michael.l@oriontech.com', '452 Skyward Drive', 'Austin', 'TX', 73301, 204, 7500.00, 'www.oriontech.com'),
    ('Quantum Networks LLC', 'Sarah Martinez', 'Marketing Manager', NULL, '5557654321', NULL, 'sarah.m@quantumnetworks.com', '789 Future Lane', 'Miami', 'FL', 33101, 204, 6000.00, 'www.quantumnetworks.com'),
    ('Omega Enterprises', 'Daniel Turner', 'Chief Financial Officer', '5550123456', '5559876543', NULL, 'daniel.t@omegaenterprises.com', '789 Horizon Blvd', 'Boston', 'MA', 02101, 204, 120000.00, 'www.omegaenterprises.com'),
    ('Infinity Systems', 'Karen White', 'Product Development Lead', '5557890123', '5553210987', '5550975465', 'karen.w@infinitysystems.com', '890 Innovation Square', 'Chicago', 'IL', 60601, 204, 65000.00, 'www.infinitysystems.com');
    
-- Concatenating to ensure the '$' symbol shows up in output with correct decimal points and places
SELECT
    creditLimit,
    CONCAT('$', FORMAT(CAST(creditLimit AS DECIMAL(10,2)), 2)) AS formattedCreditLimit
FROM Customers

-- Viewing the data from customers table, remembering to use the concatenated function to view $ sign in new field, formattedCreditLimit
SELECT
    *,
    CONCAT('$', FORMAT(creditLimit, 2)) AS formattedCreditLimit
FROM Customers;

-- Creating the third table, PProducts
CREATE TABLE IF NOT EXISTS PProducts(
	productCode INT PRIMARY KEY NOT NULL AUTO_INCREMENT,     -- set to auto increment each product code should be unique automatically, primary key
    name VARCHAR(500) UNIQUE NOT NULL DEFAULT '',       -- setting character limits and setting strings to default '' empty strings
    description VARCHAR(500) NOT NULL DEFAULT '',
    suggestedUnitPrice DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (suggestedUnitPrice >= 0.00),       -- ensuiring that the decimal places for anything that requires a decimal are set, ensuring that unitprice is always positive and not negative
    buyUnitPrice DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (buyUnitPrice >= 0.00),     
    unitsInStock INT NOT NULL DEFAULT 0 CHECK (unitsInStock >= 0),       -- ensuring units in stock are always positive 
    unitsOnOrder INT DEFAULT 0 CHECK (unitsOnOrder >= 0),                 -- ensuring units on order are always positive 
    reorderLevel INT NOT NULL DEFAULT 0 CHECK (reorderLevel >= 0),           -- ensuring reorder units are always positive 
    supplierID INT NOT NULL              
) AUTO_INCREMENT=3000; 

-- Inserting data into the PProducts Table
INSERT INTO PProducts (name, description, suggestedUnitPrice, buyUnitPrice, unitsInStock, unitsOnOrder, reorderLevel, supplierID) VALUES
	('Wireless Mouse', 'Ergonomic wireless mouse with adjustable DPI and long battery life', 25.00, 15.00, 100, 50, 10, 2100),
    ('Bluetooth Headphones', 'Noise-canceling over-ear Bluetooth headphones with 20-hour battery life', 80.00, 50.00, 150, 30, 20, 2101),
    ('Laptop Stand', 'Adjustable laptop stand with cooling pad and ergonomic design', 45.00, 30.00, 75, 40, 15, 2102),
    ('Smartphone Charger', 'Fast-charging USB-C smartphone charger', 20.00, 10.00, 200, 100, 30, 2103),
    ('LED Desk Lamp', 'LED desk lamp with touch control and adjustable brightness', 35.00, 20.00, 50, 25, 5, 2104);

-- Contactenating so that the $ sign shows
SELECT 
    suggestedUnitPrice,
    buyUnitPrice,
    CONCAT('$', FORMAT(suggestedUnitPrice, 2)) AS formattedSuggestedUnitPrice,
    CONCAT('$', FORMAT(buyUnitPrice, 2)) AS formattedBuyUnitPrice
FROM PProducts;

-- Viewing the data from PProducts table, remembering to use the concatenated function to view $ sign in new fields, formattedSuggestedUnitPrice and formattedBuyUnitPrice
SELECT
	*,
    suggestedUnitPrice,
    buyUnitPrice,
    CONCAT('$', FORMAT(suggestedUnitPrice, 2)) AS formattedSuggestedUnitPrice,
    CONCAT('$', FORMAT(buyUnitPrice, 2)) AS formattedBuyUnitPrice
FROM PProducts;

-- Creating the fourth table, Orders
CREATE TABLE IF NOT EXISTS Orders(
	orderID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,           -- setting auto increment, unique automatically primary key
    customerID INT NOT NULL,                       -- field cannot be null
    employeeID INT NOT NULL,
    orderedDate DATETIME NOT NULL DEFAULT '1001-01-01',       -- using datetime function here with a default, no null values
    orderStatus ENUM('In progress', 'Fulfilled', 'Received') NOT NULL,    -- setting to ENUM so that there aer 3 choices an order can be set to field cannot be null
	shippedDate DATETIME NOT NULL DEFAULT '1001-01-01', 
    shipperID INT NOT NULL, 
    shipToName VARCHAR(80) NOT NULL,  -- setting each character field values and not null
    shiptoAddress VARCHAR(40) NOT NULL,
    shipToCity VARCHAR(50) NOT NULL,
    shipToCountry VARCHAR(50) NOT NULL,
    shiptoPostalCode VARCHAR(20) NOT NULL,
    FOREIGN KEY (customerID) REFERENCES Customers (customerID),        -- setting up foreign keys that each reference different tables to this one
	FOREIGN KEY (employeeID) REFERENCES Employees (employeeID),
    FOREIGN KEY (shipperID) REFERENCES Shippers (shipperID)
) AUTO_INCREMENT=4000;

-- Entering data into Orders table
INSERT INTO Orders (customerID, employeeID, orderedDate, orderStatus, shippedDate, shipperID, shipToName, shipToAddress, shipToCity, shipToCountry, shipToPostalCode) VALUES
	(100, 204, '2024-11-12', 'In Progress', '1001-01-01', 8000, 'Emily Johnson', '123 Innovation Blvd', 'Phoenix', 'USA', 85001),
    (101, 204, '2024-11-10', 'Fulfilled', '2024-11-11', 8001, 'Michael Lee', '452 Skyward Drive', 'Austin', 'USA', 73301),
    (102, 204, '2024-11-08', 'Received', '2024-11-09', 8002, 'Sarah Martinez', '789 Future Lane', 'Miami', 'USA', 33101),
    (103, 204, '2024-10-10', 'Received', '2024-10-14', 8003, 'Daniel Turner', '789 Horizon Blvd', 'Boston', 'USA', 02101),
    (104, 204, '2024-11-10', 'In Progress', '1001-01-01', 8004, 'Karen White', '890 Innovation Square', 'Chicago', 'USA', 60601);
    
-- Viewing the data from the Orders Table
SELECT * FROM Orders

-- Creating the fifth table, Invoices
CREATE TABLE IF NOT EXISTS Invoices(
	invoiceID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,       -- setting primary key and auto increment, cannot be null
    orderID INT NOT NULL,
    invoiceDate DATETIME NOT NULL DEFAULT '1001-01-01',    -- using datetime function cannot be null set default
    invoiceStatus ENUM('Paid', 'Unpaid', 'Partially Paid', 'Pending Payment') NOT NULL,   -- using ENUM to ensure that all orders have a certain status with them
    FOREIGN KEY (orderID) REFERENCES Orders (orderID)        -- setting foreign key to reference order table
) AUTO_INCREMENT=500;

-- Inserting data into the fifth table, Invoices
INSERT INTO Invoices (orderID, invoiceDate, invoiceStatus) VALUES
	(4000, '1001-01-01', 'Partially Paid'),
    (4001, '2024-11-10', 'Paid'),
    (4002, '2024-11-08', 'Paid'),
    (4003, '2024-10-10', 'Paid'),
    (4004, '2024-11-10', 'Unpaid');

-- Viewing data from the Invoices Table
SELECT * FROM Invoices

-- Creating the sixth table, ProductDetails
CREATE TABLE IF NOT EXISTS ProductDetails(   
	productCode INT PRIMARY KEY NOT NULL,       -- setting productcode created from products table, is foreign key below
    moreDescription VARCHAR(600),           -- adding character limits for more description, can be null 
    image BLOB,         -- images are blob file types
    comment VARCHAR(600),               -- adding character limits for more description, can be null 
    htmlDescription VARCHAR(600) UNIQUE, -- adding character limits for more description, can be null, should be unique as every product is different 
    FOREIGN KEY (productCode) REFERENCES PProducts (productCode) -- setting foreign key to reference the products table
);

-- Inserting data into the ProductDetails table
INSERT INTO ProductDetails (productCode, moreDescription, image, comment, htmlDescription) VALUES
	(3000, 'Ergonomic wireless mouse with adjustable DPI and long battery life', '/images/wirelessmouse.jpg', 'Smooth performance, but the scroll wheel could be more precise',
		'<p>Boost your productivity with this ergonomic wireless mouse featuring adjustable DPI settings and a long-lasting battery.</p>'),
	(3001, 'High-quality Bluetooth headphones with noise-cancelling technology', '/images/bluetoothheadphones.jpg', 'Great sound and comfort, but the connection sometimes drops',
		'<p>Immerse yourself in crystal-clear sound with these Bluetooth headphones, equipped with noise-cancelling technology for uninterrupted listening. </p>'),
	(3002, 'Adjustable laptop stand for ergonomic comfort and improved posture', '/images/laptopstand/.jpg', 'Stable and easy to adjust, but a bit bulky to carry around', '<p>Elevate your workspace with this adjustable laptop stand,
		designed to reduce neck strain and improve posture during long hours of work.</p>'),
	(3003, 'Fast-charging smartphone charger with USB-C and lightning cable support', '/images/smartphonecharger/.jpg', 'charges quickly, but the cable is a bit stiff', '<p>Keep your devices powered up with this fast-charging smartphone charger,
		compatible with both USB-C and Lightning cables for quick and efficient charging.</p>'),
	(3004, 'LED desk lamp with adjustable brighness and color temperature', '/images/leddesklamp.jpg', 'Versatile lighting, but the base could be more sturdy', '<p>Illuminiate
		your workspace with this LED desk lamp, featuring adjustable brighness and color temperature to reduce eye strain and enhance focus.</p>');
        
-- Viewing data in the ProductDetails Table
SELECT * FROM ProductDetails
    
-- Creating the seventh table, OrderLines
CREATE TABLE IF NOT EXISTS OrderLines(            
	orderID INT NOT NULL,        -- setting orderid to not null, below is a foreign key
    productCode INT NOT NULL,          
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),       -- setting quantity limits so that they are not 0, is an integer
    unitPrice DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (unitPrice >= 0.00),       -- setting unit prices so that they are always positive, no null fields, with decimal place formatting 
    lineNumber INT NOT NULL,
    PRIMARY KEY (orderID, productCode),        -- setting primary keys (there are two for this table and need to be set at end)
    FOREIGN KEY (orderID) REFERENCES Orders (orderID),       -- setting foreign key to orders table
	FOREIGN KEY (productCode) REFERENCES PProducts (productCode)      -- setting forein key to products table
);

-- Ensuring the $ sign shows up in output
SELECT
	unitPrice,
    CONCAT('$', FORMAT(unitPrice, 2)) AS formattedUnitPrice
FROM OrderLines;

-- Inserting data into OrderLines table
INSERT INTO OrderLines (orderID, productCode, quantity, unitPrice, lineNumber) VALUES
	(4000, 3003, 60, 20.00, 1),
    (4000, 3002, 40, 45.00, 2),
    (4000, 3000, 60, 25.00, 3),
    (4001, 3001, 45, 80.00, 1),
    (4002, 3004, 85, 35.00, 1);
    
-- Viewing the data from OrderLines table, remembering to use the contact to see the formatted prices
SELECT
	*,
	unitPrice,
    CONCAT('$', FORMAT(unitPrice, 2)) AS formattedUnitPrice
FROM OrderLines;

-- Creating the eighth table, Shippers
CREATE TABLE IF NOT EXISTS Shippers(
	shipperID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,   -- set to auto increment, primary key, field cannot be null, will be unique by default of auto increment 
    companyName VARCHAR(50) NOT NULL DEFAULT '',    -- setting character limits and default empty string
    contactName VARCHAR(50) NOT NULL DEFAULT '',
    contactPhone CHAR(10) UNIQUE NOT NULL DEFAULT ''
)AUTO_INCREMENT=8000;

-- Inserting data into the eigth table, Shippers
INSERT INTO Shippers (companyName, contactName, contactPhone) VALUES
	('Oceanic Freight Co.', 'Mike Rodriguez', '5551012345'),
    ('Global Cargo Solutions', 'Laura Simmons', '5552023456'),
    ('Horizon Shipping Lines', 'Priya Deshmukh', '5553034567'),
    ('Rapid Logistics Partners', 'James McAllister', '5554046789'),
    ('SwiftPort Delivery Group', 'Emma Carter', '5555056789');
    
-- Viewing data from the Shippers table
SELECT * FROM Shippers
    
-- Creating the ninth table, Payments
CREATE TABLE IF NOT EXISTS Payments(
	orderID INT PRIMARY KEY NOT NULL,       -- setting primary key also foreign key referencing orders table below, field cannot be null
    paymentDate DATETIME NOT NULL DEFAULT '1001-01-01',       -- using datetime for this field with a default date, field cannot be null
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (amount >= 0.00),  -- setting decimal place formatting, checking amount so that it cannot be negative, field canno tbe null
    checkNumber VARCHAR(6) DEFAULT 0000,     -- setting character limits and default for check number, can be null because some customers may pay by credit card and not check
    FOREIGN KEY (orderID) REFERENCES Orders (orderID)      -- foreign key references orders table
);

-- Inserting data into the ninth table, Payments
INSERT INTO Payments (orderID, paymentDate, amount, checkNumber) VALUES
	(4000, '1001-01-01', '4743.64', 1897),
    (4001, '2024-11-10', '3884.23', NULL),
    (4002, '2024-11-08', '3142.78', 7412),
    (4003, '2024-10-10', '810.91', NULL),
    (4004, '2024-11-10', '1623.49', NULL);
    
-- Concatenating so that the $ sign shows up in output
SELECT
	amount,
    CONCAT('$', FORMAT(amount, 2)) AS formattedAmount
FROM Payments;

-- Viewing data from the Payments table with all and formatted amount
SELECT
	*,
	amount,
    CONCAT('$', FORMAT(amount, 2)) AS formattedAmount
FROM Payments;
    
-- Creating the tenth table, InvoiceLines
CREATE TABLE IF NOT EXISTS InvoiceLines(
	invoiceID INT NOT NULL,       -- setting as int, cannot be null
    productCode INT NOT NULL,
    unitPrice DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (unitPrice >= 0.00),    -- setting unit price so that decimal formatting is correct and prices are always positive  cannot be null
    unitsShipped INT NOT NULL DEFAULT 0 CHECK (unitsShipped >= 0),          -- setting units so that they are always positive cannot be null default is 0
    LineNumber INT NOT NULL DEFAULT 00,               -- line number cannot be null, is an int
    PRIMARY KEY (invoiceID, productCode),     -- setting two  primary keys for this table
    FOREIGN KEY (invoiceID) REFERENCES Invoices (invoiceID),      -- setting foreign key to reference invoice table
    FOREIGN KEY (productCode) REFERENCES PProducts (productCode)      -- setting foreign key to reference products table
);
    
-- Inserting data into the tenth table, InvoiceLines
INSERT INTO InvoiceLines (invoiceID, productCode, unitPrice, unitsShipped, LineNumber) VALUES
	(500, 3003, 20.00, 60, 1),
    (500, 3002, 45.00, 40, 2),
    (500, 3000, 25.00, 60, 3),
    (501, 3001, 80.00, 45, 1),
    (502, 3004, 35.00, 85, 1);

-- Concatenating to ensure the '$' shows up in output    
SELECT
	unitPrice,
    CONCAT('$', FORMAT(unitPrice, 2)) AS formattedUnitPrice
FROM InvoiceLines;

-- Viewing data with all and concatenated prices in the InvoiceLines table
SELECT
	*,
    	unitPrice,
    CONCAT('$', FORMAT(unitPrice, 2)) AS formattedUnitPrice
FROM InvoiceLines;

-- Creating the eleventh table, Supplies
CREATE TABLE IF NOT EXISTS Supplies(
	supplierID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,    -- supplierid sent to auto increment cannot be null, is primary key
    companyName VARCHAR(50) NOT NULL DEFAULT '',           -- setting character limits for fields, cannot be empty, setting default empty string
    contactName VARCHAR(50) NOT NULL DEFAULT '',
    contactJobTitle VARCHAR(100) NOT NULL DEFAULT '',
    phoneOffice CHAR(10) UNIQUE DEFAULT '',         -- this should be a unique field
    phoneMobile CHAR(10) UNIQUE NOT NULL DEFAULT '',
    fax CHAR(10) UNIQUE DEFAULT '',           -- this should be  a unique field, can be null
    email VARCHAR(60) UNIQUE NOT NULL DEFAULT '',
    address VARCHAR(60) NOT NULL DEFAULT '',
    city VARCHAR(60) NOT NULL DEFAULT '',
    country VARCHAR(50) NOT NULL,
    PostalCode VARCHAR(20) NOT NULL,
    homePage VARCHAR(400) UNIQUE NOT NULL
)AUTO_INCREMENT = 2100;

-- Inserting data into the eleventh table, Supplies
INSERT INTO Supplies (companyName, contactName, contactJobTitle, phoneOffice, phoneMobile, fax, email, address, city, country, PostalCode, homePage) VALUES
	('Oceanic Freight Co.', 'Mike Rodriquez', 'Operations Manager', '5551012345', '5557894444', '5555552222', 'laura@oceanicfreight.com', '123 Harbor Way', 'Miami', 'USA', '33101', 'www.oceanicfreight.com'),
    ('Global Cargo Solutions', 'Laura Simmons', 'Logistics Coordinator', '5552023456', '5551236666', NULL, 'mike@globalcargo.com', '456 Trade Lane', 'Houston', 'USA', '77002', 'www.globalcargo.com'),
    ('Horizon Shipping Lines', 'Priya Deshmukh', 'Customer Service Lead', NULL, '5553034567', NULL, 'priya@horizonshipping.com', '789 Dock Street', 'Los Angeles', 'USA', '90012', 'www.horizonshipping.com'),
    ('Rapid Logistics Partners', 'James McAallister', 'Regional Director', '5554045678', '5554446666', '5554789999', 'mcjames@rapidlog.com', '321 Express Road', 'Atlanta', 'USA', '30303', 'www.rapidlogistics.com'),
    ('SwiftPort Delivery Group', 'Emma Carter', 'Senior Account Manager', '5555056789', '5555559999', NULL, 'emma@swiftport.com', '654 Gateway Avenue', 'Seattle', 'USA', '98101', 'www.swiftport.com');
    
-- Viewing the data in the Supplies table
SELECT * FROM Supplies
    
-- Creating the twwelfth table, ProductSupplier
CREATE TABLE IF NOT EXISTS ProductSupplier(
	productCode INT NOT NULL,      -- setting to an int cannot be null, foriegn key below
    supplierID INT NOT NULL,      -- setting to an int cannot be null, foriegn key below
    PRIMARY KEY (productCode, supplierID), -- two foreign keys for this table
    FOREIGN KEY (productCode) REFERENCES PPRoducts (productCode), -- setting foreign key to reference products table
    FOREIGN KEY (supplierID) REFERENCES Supplies (supplierID), -- setting foreign key to reference supplies table
    notes VARCHAR(400) -- setting character limits for notes
);

-- Inserting data into the twelfth table, ProductSupplier
INSERT INTO ProductSupplier (productCode, supplierID, notes) VALUES
	(3000, 2100, 'Recently expanded to include eco-friendly shipping options.'),
    (3001, 2101, 'Offers custom packaging and tracking solutions for fragile items'),
    (3002, 2102, 'Recently invested in AI-driven logistics to improve efficiency.'),
    (3003, 2103, 'Known for its same-day delivery services in urban areas.'),
    (3004, 2104, 'Offers specialized services for perishable goods and temperature-sensitive items.');
    
-- Viewing the data from the ProductSupplier table
SELECT * FROM ProductSupplier
    
-- Creating indexes for all major primary keys and foreign keys for faster searching
CREATE INDEX idx_supplierID ON PProducts (supplierID);
CREATE INDEX idx_reportsTo ON Employees(reportsTo);
CREATE INDEX idx_salesRepID ON Customers(salesRepID);
-- CREATE INDEX idx_customerID ON Orders(customerID);  -- already used as FK constraint, but added anyway (did not run, just commented out to show that it is there, per the directions)
CREATE INDEX idx_employeeID ON Orders(employeeID);
CREATE INDEX idx_shipperID ON Orders(shipperID);
-- CREATE INDEX idx_orderID_invoices ON Invoices(orderID);  -- already used as FK constraint, but added anyway (did not run, just commented out to show that it is there, per the directions)
CREATE INDEX idx_productCode ON ProductDetails(productCode);
CREATE INDEX idx_orderID_orderLines ON OrderLines(orderID);
CREATE INDEX idx_productCode_orderLines ON OrderLines(productCode);
CREATE INDEX idx_orderID_payments ON Payments(orderID);
