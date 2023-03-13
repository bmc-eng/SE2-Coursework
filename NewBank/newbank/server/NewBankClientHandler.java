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
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	public void welcomeMenu(){
		// 
		out.println("Welcome to NewBank!");
		out.println("1. Login");
		out.println("2. Create account");
		out.println("Enter option (number)");
		try{
			String response = in.readLine();
			if (response == "1"){
				login();
			}
			if (response == "2"){
				createAccount();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public void login(){
		try{
			out.println("Enter Username");
			String userName = in.readLine();
			// ask for password
			out.println("Enter Password");
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them 
			if(customer != null) {
				out.println("Log In Successful. What do you want to do?");
				while(true) {
					String request = in.readLine();
					System.out.println("Request from " + customer.getKey());
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
			out.println("name: ");
			String key = in.readLine();
			out.println("email: ");
			String email = in.readLine();
			out.println("address: ");
			String address = in.readLine(); //TODO we should probably make addresses their own class so that they can be printed neatly
			out.println("phone number: ");
			String phone = in.readLine();
			Customer newCustomer = new Customer(email, address, phone);
			bank.addNewCustomer(newCustomer, key); //TODO This uses the name for the key; this should probably be changed to something else and the name added to the customer class
			// need to add some way of adding the new customer to the hashmap in the bank object.
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
