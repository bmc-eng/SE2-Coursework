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
				while(true) {
					String request = in.readLine();
					System.out.println("Request from " + customerID.getKey());
					// Refresh customer details:
					customer = db.getCustomer(userName, true);
					// Changed to customer object
					String responce = bank.processRequest(customer, request);
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

	public void createAccount() throws IOException{
		try{
			out.println("Username: ");
			String userName = in.readLine();

			// TODO: Check if the username already exists

			out.println("Password: ");
			String password = in.readLine();
			// TO DO: Encrypt password before writing to database
			out.println("First Name: ");
			String firstName = in.readLine();
			out.println("Last Name: ");
			String lastName = in.readLine();
			out.println("email: ");
			String email = in.readLine();
			out.println("address: ");
			String address = in.readLine(); //TODO we should probably make addresses their own class so that they can be printed neatly
			out.println("phone number: ");
			String phone = in.readLine();
			Customer newCustomer = new Customer(userName, password, firstName, lastName, address, email);
			// Write the customer data to the database - using serialisation
			db.addCustomer(newCustomer, true);
		}
		catch(IOException e){
			System.out.println("Account creation failed; please try again");
			createAccount();
		}
		finally{
			System.out.println("Creation Successful");
		}
	}

	public void run() {
		// keep getting requests from the client and processing them
			welcomeMenu();
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
