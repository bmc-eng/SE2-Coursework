package newbank.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
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
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);

		serializeCustomers("bhagy", bhagy);
		serializeCustomers("christina", christina);
		serializeCustomers("john", john);
		

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
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			default : return "FAIL";
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	public void addNewCustomer(Customer newCustomer, String key){
		customers.put(key, newCustomer);
	}

}
