package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private Database db;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		db = new Database();
	}

	// Method to set up and start the session on behalf of a user
	private void setupServer(){
		
		try{
			// get the initial setup string from the client
			String initialSetup = in.readLine();
			out.println("Checking Details...");

			System.out.println(initialSetup);
			String[] details = initialSetup.split("\t");
			String userName = details[1];
			String password = details[2];

			// Check if this is a new user or existing login
			Customer customer;
			if (details[0].equals("LOGIN")){
				// login the user
				customer = loginUser(userName, password, details);
			} else if (details[0].equals("CREATE")){
				customer = createAccount(userName, password, details);
			} else {
				customer = null;
			}



			CustomerID customerID = bank.checkLogInDetails(userName, password, customer);
			
			// if the user is authenticated then get requests from the user and process them 
			if(customerID != null) {
				out.println("Log In Successful. What do you want to do?");
				boolean isOngoingSession = true;
				while(isOngoingSession) {
					String request = in.readLine();
					System.out.println("Request from " + customerID.getKey());
					// Refresh customer details:
					customer = db.getCustomer(userName, true);
					// Changed to customer object
					String responce = bank.processRequest(customer, request);
					
					// gracefully exit the process
					if (responce == "LOGGING OFF..."){
						isOngoingSession = false;
					}
					out.println(responce);
				}
			}
			else {
				out.println("Log In Failed");
			}



		} catch (IOException ioe){
			ioe.printStackTrace();

		}
		

	}

	// Method to log in a user and return the customer
	private Customer loginUser(String userName, String password, String[] details){
		
		// Check that the user exists
		Customer customer = db.getCustomer(userName, true);
		if (customer == null){
			out.println("Username does not exist...");
			return null;
		}

		if(!customer.getPassword().contentEquals(password)){
			out.println("Password invalid");
			return null;
		}

		return customer;
		
	}

	/* 
	public void welcomeMenu(){
		// 
		out.println("Welcome to NewBank!");
		out.println("1. Login");
		out.println("2. Create account");
		out.println("3. Exit");
		out.println("Enter option (number)");
		try{
			String response = in.readLine();
			if (response.contains("1")){
				login();
			}
			if (response.contains("2")){
				createAccount();
			}
			if (response.contains("3")){
				// exit the thread
				return;
			}
		}
		catch(IOException e){
			e.printStackTrace();
			
		}
		// Recursively remain on this menu until either 1 or 2 is selected
		welcomeMenu();
	}
	
	
	public void login(){
		
		try{
			out.println("Enter Username");
			String userName = in.readLine();
			
			// Check if the username is in the database
			Customer customer = db.getCustomer(userName, true);
			if (customer == null){
				out.println("Username does not exist...");
				welcomeMenu();
			}
			// ask for password - use Console to mask the password from screen
			out.println("Enter Password");
			String password = in.readLine();

			out.println("Checking Details...");
			if(!customer.getPassword().contentEquals(password)){
				out.println("Password invalid");
				welcomeMenu();
			}
			
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customerID = bank.checkLogInDetails(userName, password, customer);
			// if the user is authenticated then get requests from the user and process them 
			if(customerID != null) {
				out.println("Log In Successful. What do you want to do?");
				boolean isOngoingSession = true;
				while(isOngoingSession) {
					String request = in.readLine();
					System.out.println("Request from " + customerID.getKey());
					// Refresh customer details:
					customer = db.getCustomer(userName, true);
					// Changed to customer object
					String responce = bank.processRequest(customer, request);
					
					// gracefully exit the process
					if (responce == "LOGGING OFF..."){
						isOngoingSession = false;
					}
					out.println(responce);
				}
			}
			else {
				out.println("Log In Failed");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	*/

	// Method to create a new customer account
	private Customer createAccount(String userName, String password, String[] details){
		
			
		//Check if the username already exists
		if (db.getCustomer(userName, true) != null){
			out.println("Username: " + userName + " already exists. Please chose another login name!");
			return null;
		}
		
		String firstName = details[3];
		String lastName = details[4];
		String email = details[5];
		String address = details[6]; //TODO we should probably make addresses their own class so that they can be printed neatly
		//String phone = details[7];
		Customer newCustomer = new Customer(userName, password, firstName, lastName, address, email);
		// Write the customer data to the database - using serialisation
		db.addCustomer(newCustomer, true);
		return newCustomer;
		
	}

	// Method run each time that requests come from the client
	public void run() {
		// keep getting requests from the client and processing them
			setupServer();
			// ask for user name
			
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
	}

}
