package newbank.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	private Database db = new Database();
	
	private NewBank() {
		customers = new HashMap<>();
		try {
			addTestData();
		} catch (IOException ioe){
			System.out.println("Error");
		}
		
	}
	
	private void addTestData() throws IOException {
		System.out.println("Setting up...");
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0, "Current"));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0,"Current"));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0,"Current"));
		customers.put("John", john);

		Customer test = new Customer("test", "test123", "T.", 
							"Est", "London", "test@test.com");
		test.addAccount(new Account("Checking",1000.0,"Current"));

		serializeCustomers("bhagy", bhagy);
		serializeCustomers("christina", christina);
		serializeCustomers("john", john);
		serializeCustomers("test", test);
		

	}

	private void serializeCustomers(String name, Customer c) throws IOException{
		// Try to serialise the data
		try {
			FileOutputStream fos = new FileOutputStream("../NewBank/newbank/server/DatabaseFiles/" 
													+ name.toLowerCase() + ".obj");
			ObjectOutputStream objectOutputStream  = new ObjectOutputStream(fos);
    		objectOutputStream.writeObject(c);
    		objectOutputStream.flush();
    		objectOutputStream.close();
		} catch(IOException ioe) {
			System.out.println("Error: " + ioe.toString());
		}
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	// Added code for serialised users 
	public synchronized CustomerID checkLogInDetails(String userName, String password, Customer customer) {
		if (customer.getUsername().contains(userName) && customer.getPassword().contains(password)){
			return new CustomerID(userName);
		}
		return null;
		/* 
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
		*/
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(Customer customer, String request) {
		try{
			switch(request) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			default : return "FAIL";
			case "NEWCURRENT" : return addCurrentAccount(customer);
		}
		}
		catch( Exception e){
			return e.getMessage(); // Return error message for resolving errors in development phase
			//return "FAIL";
		}
	}
	
	private String showMyAccounts(Customer customer) {
		return customer.accountsToString();
	}

	public String addCurrentAccount(Customer customer){
		// Check if customer has an existing current account
		for(Account a: customer.getAccounts()){
			if (a.getType().contentEquals("Current")){
				return "Current account already exists!";
			}
		}
		Account account = new Account(customer.getFirstName()+"Current", 0, "Current");
		customer.addAccount(account);
		db.addCustomer(customer, true);
		// Need to re-serialise the object to save changes
		return "Current account added";
		
	}

	public void addNewCustomer(Customer newCustomer, String key){
		customers.put(key, newCustomer);
	}

}
