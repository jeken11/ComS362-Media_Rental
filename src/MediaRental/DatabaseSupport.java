package MediaRental;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DatabaseSupport
{
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/movie_rental";
    public static final String USER = "root";
    public static final String PASSWORD = "pass";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn;

    public DatabaseSupport()
    {
        try
        {
            Class.forName(DB_DRIVER);
        } catch (Exception E)
        {
            System.err.println("Unable to load driver.");
            E.printStackTrace();
        }
        try
        {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
    }
    
    public boolean addFrequentCustomerStrategy(FrequentCustomerStrategy strategy){
        String statement = "INSERT INTO FrequentCustomerStrategy (name, fixedPoints, pointsPerDay) VALUES " +
                           "('" + strategy.getName() + "', '" + strategy.getFixedPoints() + "', '" + strategy.getPointsPerDay() + "');";
        try
        {
            PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            stmt1.executeUpdate();
            return true;

        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return false;
        }
    }
    
    public static FrequentCustomerStrategy getFrequentCustomerStrategy(String name)
    {
        String statement = "Select name, fixedPoints, pointsPerDay from FrequentCustomerStrategy where name = '" + name + "';";
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            rs1.next();
            int fixedPoints = rs1.getInt("fixedPoints");
            int ppd = rs1.getInt("pointsPerDay");
            FrequentCustomerStrategy strategy = new FrequentCustomerStrategy(fixedPoints, ppd, name);
            return strategy;
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
    }
    
    public boolean addRentalPricingStrategy(RentalPricingStrategy pricing){
        String statement = "INSERT INTO RentalPricingStrategy (name, standardRentalLength, dailyOverdueCharge, standardRentalCharge) VALUES " +
                           "('" + pricing.getName() + "', '" + pricing.getStandardRentalLength() + "', '" + pricing.getDailyOverdueCharge() + "', '" + pricing.getStandardRentalCharge() + "');";
        try
        {
            PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            stmt1.executeUpdate();
            return true;

        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return false;
        }
    }

    public static RentalPricingStrategy getRentalPricingStrategy(String name)
    {
        String statement = "SELECT name, standardRentalLength, dailyOverdueCharge, standardRentalCharge FROM RentalPricingStrategy WHERE name = '" + name + "';";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery(statement);
            rs1.next();
            double stdCharge = rs1.getDouble("standardRentalCharge");
            int rentalLength = rs1.getInt("standardRentalLength");
            double dailyCharge = rs1.getDouble("dailyOverdueCharge");
            return new RentalPricingStrategy(stdCharge, rentalLength, dailyCharge, name);
        }
        catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
    }

    /**
     * @param title - can be null
     * @param genre - can be null
     * @return customers that have a name or address similar to the passed in values
     */
    public ArrayList<Product> findProducts(String title, String genre)
    {
        String statement = "Select id, title, genre, description, catalogID from ProductCatalog";
        String whereClause = "";
        ArrayList<Product> products = new ArrayList<Product>();
        if (title != null && !title.isEmpty())
        {
            whereClause += "where title like %'" + title + "%'";
            if (genre != null && !genre.isEmpty())
            {
                whereClause += "and genre like %'" + genre + "%'";
            }
        } else if (genre != null && !genre.isEmpty())
        {
            whereClause += "where genre like %'" + genre + "%'";
        }
        statement += whereClause + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            while (rs1.next())
            {
                int id = rs1.getInt("id");
                String t = rs1.getString("title");
                String g = rs1.getString("genre");
                String desc = rs1.getString("description");
                Product prod = new Product(title, "", g, desc, id, false);
                prod.setGenre(g);
                prod.setCatalogId(id);
                products.add(prod);
            }
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        return products;

    }

    /**
     * @param name    - can be null
     * @param address - can be null
     * @return customers that have a name or address similar to the passed in values
     */
    public static ArrayList<Customer> findCustomers(String name, String address)
    {
        String statement = "Select id, name, address from Customer";
        String whereClause = "";
        ArrayList<Customer> customers = new ArrayList<Customer>();
        if (name != null && !name.isEmpty())
        {
            whereClause += "where name like %'" + name + "%'";
            if (address != null && !address.isEmpty())
            {
                whereClause += "and address like %'" + address + "%'";
            }
        } else if (address != null && !address.isEmpty())
        {
            whereClause += "where address like %'" + address + "%'";
        }
        statement += whereClause + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            while (rs1.next())
            {
                int id = rs1.getInt("id");
                String n = rs1.getString("name");
                String a = rs1.getString("address");
                Customer cust = new Customer(name, address);
                cust.setId(id);
                customers.add(cust);
            }
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        return customers;

    }
    
    public static Transaction getTransaction(int id){
        try {
            String statement = "Select customerID from Transaction where id = " + id + ";";
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            rs1.next();
            int customerID = rs1.getInt("customerID");
            Customer customer = getCustomer(customerID);
            Transaction transaction = getTransactionWithoutCustomer(id);
            transaction.setCustomer(customer);
            return transaction;
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
        
    }
    
    private static Transaction getTransactionWithoutCustomer(int id)
    {
        String statement = "Select id, customerID, statement, paid from Transaction where id = " + id + ";";
        ArrayList<Rental> rentals = new ArrayList<Rental>();
        ArrayList<Sale> sales = new ArrayList<Sale>();
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            rs1.next();
            int customerID = rs1.getInt("customerID");
            String transactionStatement = rs1.getString("statement");
            Boolean paid = rs1.getBoolean("paid");
            String statement2 = "Select id from Rental where transactionID = " + id + ";";
            ResultSet rs2 = stmt1.executeQuery(statement2);
            while (rs2.next())
            {
                int rentalID = rs2.getInt("id");
                Rental rental = getRental(rentalID);
                rentals.add(rental);
            }
            String statement3 = "Select id from Sale where transactionID = " + id + ";";
            ResultSet rs3 = stmt1.executeQuery(statement3);
            while (rs3.next())
            {
                int saleID = rs3.getInt("id");
                Sale sale = getSale(saleID);
                sales.add(sale);
            }

            return new Transaction(rentals, sales, paid, transactionStatement, id);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
    }
    
    private static Sale getSale(int id)
    {
        String statement = "Select id, productID, price, transactionID from Sale where id = " + id + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            rs1.next();
            int productID = rs1.getInt("productID");
            double price = rs1.getFloat("price");
            int transactionID = rs1.getInt("transactionID");
            Product product = getProduct(productID);
            return new Sale(product, price, id);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
    }
    
    private static Rental getRental(int id)
    {
        String statement = "Select id, productID, daysRented, dueDate from Rental where id = " + id + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            rs1.next();
            int productID = rs1.getInt("productID");
            Date dueDate = rs1.getDate("dueDate");
            int daysRented = rs1.getInt("daysRented");
            String dueDateString = dueDate.toString();
            Product product = getProduct(productID);
            return new Rental(product, dueDateString, id, daysRented);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
    }
    
    public static Product getProduct(int id)
    {
        String statement = "Select id, available, productCatalogID from Product where id = " + id + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            rs1.next();
            int catalogID = rs1.getInt("productCatalogID");
            boolean available = rs1.getBoolean("available");
            String statement2 = "Select id, title, description, genre, rentalStrategyName, customerStrategyName from ProductCatalog where id = " + catalogID + ";";
            ResultSet rs2 = stmt1.executeQuery(statement2);
            rs2.next();
            String title = rs2.getString("title");
            String description = rs2.getString("description");
            String genre = rs2.getString("genre");
            String rentalPricingStrategyName = rs2.getString("rentalStrategyName");
            String customerStrategyName = rs2.getString("customerStrategyName");
            FrequentCustomerStrategy cs = null;
            RentalPricingStrategy rs = null;
            if (!rentalPricingStrategyName.equals("")){
                rs = getRentalPricingStrategy(rentalPricingStrategyName);
            }
            if (!customerStrategyName.equals("")){
                cs = getFrequentCustomerStrategy(customerStrategyName);
            }
            return new Product(title, "", genre, description, id, catalogID, cs, rs, available);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
    }

    /**
     * Get a customer from the database by id
     *
     * @param id
     * @return a customer object
     */
    public static Customer getCustomer(int id)
    {
        String statement = "Select name, address from Customer where id = " + id + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(statement);
            rs1.next();
            String name = rs1.getString(1);
            String address = rs1.getString(2);
            Customer customer = new Customer(name, address, id);
            setTransactionsOnCustomer(customer);
            return customer;
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return null;
        }
    }
    
    private static void setTransactionsOnCustomer(Customer customer){
        try {
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            Statement stmt1 = conn.createStatement();
            String statement = "Select id from Transaction where customerID = " + customer.getId() + ";";
            ResultSet rs2 = stmt1.executeQuery(statement);
            while (rs2.next())
            {
                int transactionID = rs2.getInt("id");
                Transaction transaction = getTransactionWithoutCustomer(transactionID);
                transaction.setCustomer(customer);
                transactions.add(transaction);
            }
            customer.setTransactions(transactions);
        }
        catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
       
    }
   
    
    private void removeRentals(int tid){
        String statement = "delete from Rental where transactionID = " + tid + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            stmt1.execute(statement);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        
    }
    
    private void removeSales(int tid){
        String statement = "delete from Sale where transactionID = " + tid + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            stmt1.execute(statement);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }  
    }
    
    
    /**
     * Remove a customer from the db
     *
     * @param id
     */
    public void removeCustomer(int id)
    {
        String statement = "delete from Customer where id = " + id + ";";
        try
        {
            Statement stmt1 = conn.createStatement();
            stmt1.execute(statement);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
    }

    /**
     * Add a customer to the database
     *
     * @param customer object to add (id should not be set yet)
     * @return id of customer added
     */
    public int addCustomer(Customer customer)
    {
        String statement = "INSERT INTO Customer (Name, Address)" +
                " VALUES ('" + customer.getName() + "', '" + customer.getAddress() + "');";
        try
        {
            PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            stmt1.executeUpdate();
            ResultSet rs = stmt1.getGeneratedKeys();
            if (rs.next())
            {
                int id = rs.getInt(1);
                customer.setId(id);
                return (id);
            }

        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        return 0;
    }

    /**
     * Used when creating a completely new product in the store
     *
     * @param product - should have a title and a type but no id
     * @return new id of product
     */
    private int addProductToCatalog(Product product)
    {
        if (product.getTitle() == null){
            return 0;
        }
        String statement;
        String strategyName = "";
        String rentalStrategyName = "";
        FrequentCustomerStrategy strategy = product.getCustomerStrategy();
        RentalPricingStrategy rental_pricing_strategy = product.getRentalPricingStrategy();
        if (strategy != null){
            strategyName = strategy.getName();
        }
        if (rental_pricing_strategy != null){
            rentalStrategyName = rental_pricing_strategy.getName();
        }
        if (product.getCatalogId() > 0){
            statement = "Update ProductCatalog set title = '" + product.getTitle() + "', genre = '" + product.getGenre() + "', customerStrategyName = '" + strategyName + "', rentalStrategyName = '" + rentalStrategyName + "' where id = " + product.getCatalogId() + ";";
        }
        else {
            statement = "INSERT INTO ProductCatalog (title, genre, customerStrategyName, rentalStrategyName)" +
                    " VALUES ('" + product.getTitle() + "', '" + product.getType() + "', '" + strategyName +  "', '" + rentalStrategyName + "');";
        }
        
        try
        {
            PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            stmt1.executeUpdate();
            ResultSet rs = stmt1.getGeneratedKeys();
            if (rs.next())
            {
                int id = rs.getInt(1);
                product.setCatalogId(id);
                return id;
            }

        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        return 0;
    }

    /**
     * Add a product to the database.
     *
     * @param catalog_id: Id of the catalog item
     * @return - id of the product from the product db
     */
    public boolean putProduct(Product product, int numberToAdd)
    {
        addProductToCatalog(product);
        try{
            if (product.getId() > 0){
                String statement = "UPDATE Product set available = " + product.getAvailable() + " where id =" + product.getId() + ";"; 
                PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
                stmt1.executeUpdate();
            }
            for (int i=0; i < numberToAdd; i++){
                String statement = "INSERT INTO Product (productCatalogID, available) VALUES (" + product.getCatalogId() + ", " + product.getAvailable() +  ");";
                
                    PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
                    stmt1.executeUpdate();
                    ResultSet rs = stmt1.getGeneratedKeys();
                    if (rs.next())
                    {
                        int id = rs.getInt(1);
                        product.id = id;
                    }
            }
            return true;
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return false;
        }
        
    }

    private int addRentalToStore(Rental rental)
    {
        String statement = "INSERT INTO Rental (productID, daysRented, dueDate) VALUES (" + rental.getProduct().getId() +
                ", " + rental.getDaysRented() + ", '" + rental.getDueDate() + "');";
        try
        {
            PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            stmt1.executeUpdate();
            ResultSet rs = stmt1.getGeneratedKeys();
            if (rs.next())
            {
                int id = rs.getInt(1);
                return id;
            }

        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        return 0;
    }

    private int addSaleToStore(Sale sale)
    {
        String statement = "INSERT INTO Sale (productID, price) VALUES (" + sale.getProduct().getId() +
                ", " + sale.getPrice() + ");";
        try
        {
            PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            stmt1.executeUpdate();
            ResultSet rs = stmt1.getGeneratedKeys();
            if (rs.next())
            {
                int id = rs.getInt(1);
                sale.setId(id);
                return id;
            }

        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        return 0;
    }

    /**
     * Add a transaction to the database
     *
     * @param transaction
     * @return id of transaction
     */
    public int putTransaction(Transaction transaction)
    { 
        try
        {
            if (transaction.getId() > 0){
                String statement = "UPDATE Transaction SET statement='" + transaction.getStatement() +
                        "',paid=" + transaction.getPaid() + " WHERE id=" + transaction.getId();
                PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
                stmt1.executeUpdate();
                removeRentals(transaction.getId());
                removeSales(transaction.getId());
                ArrayList<Sale> sales = transaction.getSales();
                for (int i=0; i < sales.size(); i++){
                    addSaleToStore(sales.get(i));
                    addSaleToTransaction(sales.get(i).getId(), transaction);
                }
                ArrayList<Rental> rentals = transaction.getRentals();
                for (int i=0; i < rentals.size(); i++){
                    int id = addRentalToStore(rentals.get(i));
                    rentals.get(i).setId(id);
                    addRentalToTransaction(rentals.get(i).getId(), rentals.get(i).getDueDate(), transaction);
                }
                
            }
            else {
                System.out.println(transaction.getCustomer().getId());
                String statement = "INSERT INTO Transaction (customerID, paid) " +
                        "VALUES (" + transaction.getCustomer().getId() + ", 0);";
                PreparedStatement stmt1 = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
                stmt1.executeUpdate();
                ResultSet rs = stmt1.getGeneratedKeys();
                if (rs.next())
                {
                    int id = rs.getInt(1);
                    transaction.setId(id);
                    return id;
                }
            }
            
        return 1;
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
        return 0;
    }

    /**
     * Add a product to a transaction in the db
     *
     * @param rental_id   The ID of the product to add to the transaction.
     * @param duedate     - format YYYY-MM-DD
     * @param transaction The transaction to be added to
     */
    private void addRentalToTransaction(int rental_id, String duedate, Transaction transaction)
    {
        String statement = "UPDATE Rental SET transactionID=" + transaction.getId() +
                ", dueDate='" + duedate + "' WHERE id=" + rental_id;
        try
        {
            Statement stmt1 = conn.createStatement();
            stmt1.executeUpdate(statement);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
    }

    private void addSaleToTransaction(int sale_id, Transaction transaction)
    {
        String statement = "UPDATE Sale SET transactionID=" + transaction.getId() + " WHERE id=" + sale_id;
        try
        {
            Statement stmt1 = conn.createStatement();
            stmt1.executeUpdate(statement);
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
        }
    }

    /**
     * Create db tables. Only run this if initializing db for the very first time
     *
     * @return boolean indicating success
     */
    public boolean createTables()
    {
        String statement = "CREATE TABLE Customer ( " +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "Name VARCHAR(45) NULL, " +
                "Address VARCHAR(120) NULL, " +
                "PRIMARY KEY (id)); ";

        String statement2 = "CREATE TABLE ProductCatalog ( " +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "title VARCHAR(45) NULL, " +
                "description VARCHAR(120) NULL, " +
                "genre VARCHAR(45) NULL, " +
                "customerStrategyName VARCHAR(45) NOT NULL REFERENCES FrequentCustomerStrategy(name), " +
                "rentalStrategyName VARCHAR(45) NOT NULL REFERENCES RentalPricingStrategy(name)," +
                "PRIMARY KEY (id)); ";

        String statement3 = "CREATE TABLE Product ( " +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "productCatalogID INT NOT NULL, " +
                "available TINYINT(1) NOT NULL DEFAULT 1, " +
                "PRIMARY KEY (id), " +
                "FOREIGN KEY (productCatalogID) REFERENCES ProductCatalog(id));";

        String statement4 = "CREATE TABLE Transaction ( " +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "customerID INT NOT NULL, " +
                "statement VARCHAR(2000) NULL, " +
                "paid TINYINT(1) NULL DEFAULT 0, " +
                "PRIMARY KEY (id), " +
                "FOREIGN KEY (customerID) REFERENCES Customer(id)); ";

        String statement5 = "CREATE TABLE Sale (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "productID INT NOT NULL, " +
                "price FLOAT default 0.0, " +
                "transactionID INT NULL, " +
                "PRIMARY KEY (id), " +
                "FOREIGN KEY (transactionID) REFERENCES Transaction(id), " +
                "FOREIGN KEY (productID) REFERENCES Product(id));";

        String statement6 = "CREATE TABLE Rental (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "productID INT NOT NULL," +
                "daysRented INT default 0, " +
                "dueDate DATE NULL, " +
                "transactionID INT NULL, " +
                "PRIMARY KEY (id), " +
                "FOREIGN KEY (transactionID) REFERENCES Transaction(id), " +
                "FOREIGN KEY (productID) REFERENCES Product(id));";
       
        String statement7 = "CREATE TABLE RentalPricingStrategy (" +
                "name VARCHAR(45) NOT NULL, " +
                "standardRentalLength INT NOT NULL," +
                "dailyOverdueCharge DOUBLE NOT NULL, " +
                "standardRentalCharge DOUBLE NOT NULL, " +
                "PRIMARY KEY (name));";
       
        String statement8 = "CREATE TABLE FrequentCustomerStrategy (" +
                "name VARCHAR(45) NOT NULL, " +
                "fixedPoints INT," +
                "pointsPerDay INT, " +
                "PRIMARY KEY (name));";

        try
        {
            Statement stmt = conn.createStatement();
            stmt.execute(statement);
            stmt.execute(statement2);
            stmt.execute(statement3);
            stmt.execute(statement4);
            stmt.execute(statement5);
            stmt.execute(statement6);
            stmt.execute(statement7);
            stmt.execute(statement8);
            stmt.close();
            return true;
        } catch (SQLException E)
        {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError: " + E.getErrorCode());
            return false;
        }

    }
}