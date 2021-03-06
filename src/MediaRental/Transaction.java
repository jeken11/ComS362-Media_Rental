package MediaRental;

import java.util.ArrayList;

/**
 * Created by Obberton13 on 2/16/2015.
 */
public class Transaction
{
	protected int id;
	protected ArrayList<Rental> rentals;
	protected ArrayList<Sale> sales;
	protected Customer customer;
	protected Boolean paid;
	protected String statement;

	public Transaction(Customer customer)
	{
		this.customer = customer;
		rentals = new ArrayList<Rental>();
		sales = new ArrayList<Sale>();
		id = 0;
	}

	public Transaction(Customer customer, ArrayList<Rental> rentals, ArrayList<Sale> sales)
	{
		this.customer = customer;
		this.rentals = rentals;
		this.sales = sales;
		id = 0;
	}

	public Transaction(Customer customer, ArrayList<Rental> rentals, ArrayList<Sale> sales, Boolean paid, String statement, int id)
	{
		this.customer = customer;
		this.rentals = rentals;
		this.sales = sales;
		this.id = id;
		this.paid = paid;
		this.statement = statement;
	}
	public Transaction(ArrayList<Rental> rentals, ArrayList<Sale> sales, Boolean paid, String statement, int id)
    {
        this.rentals = rentals;
        this.sales = sales;
        this.id = id;
        this.paid = paid;
        this.statement = statement;
    }
	
	public void setPaid(boolean paid){
	    this.paid = paid;
	}
	
	public boolean getPaid(){
	    return this.paid;
	}
	
	public int getFrequentCustomerPoints(){
	    int points = 0;
	    for (int j=0; j<this.getRentals().size(); j++){
            Rental rental = this.getRentals().get(j);
            points += rental.getProduct().getCustomerStrategy().getPoints(rental.getDaysRented());
        }
	    return points;
	}
	
	public String getStatement(){
	    double      totalAmount          = 0;
        int         frequentRenterPoints = 0;
        String      result               = "<html>\n"; 
        result += "Rental Record for " + customer.getName() + "<br>\n" +
                "<p>\n";
        
       for (Rental rental : rentals) {
            
            double thisAmount = rental.getPrice();
           
            // show figures for this rental
            String className = rental.getProduct().getGenre();
            result += "\t" + className + " - " + rental.getProduct().getTitle() +
                      "\t" + String.format("%.2f", thisAmount) + "<br>\n";
            totalAmount += thisAmount;
        }
       if (sales.size() > 0){
           result += "</p>\nSale Record for " + customer.getName() + "<br>\n" +
                    "<p>\n";
               for (Sale sale : sales) {
                   
                   double thisAmount = sale.getPrice();
                  
                   // show figures for this sale
                   String className = sale.getProduct().getGenre();
                   result += "\t" + className + " - " + sale.getProduct().getTitle() +
                             "\t" + String.format("%.2f", thisAmount) + "<br>\n";
                   totalAmount += thisAmount;
               }
       }
       
        
        // add footer lines
        result += "</p>\nAmount owed is " + String.format("%.2f", totalAmount) + "<br>\n";
        result += "</html>";
        return result;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public ArrayList<Rental> getRentals()
	{
		return rentals;
	}

	public void setRentals(ArrayList<Rental> rentals)
	{
		this.rentals = rentals;
	}

	public ArrayList<Sale> getSales()
	{
		return sales;
	}

	public void setSales(ArrayList<Sale> sales)
	{
		this.sales = sales;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	public void addSale(Product product, double price)
	{
	    Sale sale = new Sale(product, price);
		sales.add(sale);
	}

	public void addRental(Rental rental)
	{
		rentals.add(rental);
	}
	public boolean pay()
	{
		paid = true;
		return paid == true;
	}
}