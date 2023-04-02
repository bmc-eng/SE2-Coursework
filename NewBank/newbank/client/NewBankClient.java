package newbank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.io.Console;


public class NewBankClient extends Thread{
	
	private Socket server;
	private PrintWriter bankServerOut;	
	private BufferedReader userInput;
	private Thread bankServerResponceThread;

	// Method to try to log into the bank -> returns a string containing the username and hashed password
	private String login(BufferedReader userInput){

		try{
			System.out.println("Enter Username");
			String userName = userInput.readLine();
			
			// ask for password - use Console to mask the password from screen
			System.out.println("Enter Password");
			String password = passwordInput();

			// Convert to dictionary - Need to implement JSON method
			HashMap<String, String> loginDetails = new HashMap<String, String>();
			loginDetails.put("userName", userName);
			loginDetails.put("password", password);

			//var jObj = new JSONObject(loginDetails);


			return "LOGIN" + "\t" + userName + "\t" + password;
		} catch (IOException ioe){
			return "Error";
		}

	}

	private String passwordInput(){
		Console console = System.console();
		char[] passwordArray = console.readPassword();
		return new String (passwordArray);
	}

	private String createAccount(BufferedReader userInput){

		try{
			System.out.println("Username: ");
			String userName = userInput.readLine();

			// Password input
			System.out.println("Password: ");
			String password = passwordInput();

			// Reenter password to check
			System.out.println("Re-enter password:");
			String repeatPassword = passwordInput();

			if (!password.equals(repeatPassword)){
				System.out.println("Passwords do not match. Try again...");
				return createAccount(userInput);
			}

			// TO DO: Encrypt password before writing to database
			System.out.println("First Name: ");
			String firstName = userInput.readLine();
			System.out.println("Last Name: ");
			String lastName = userInput.readLine();
			System.out.println("email: ");
			String email = userInput.readLine();
			System.out.println("address: ");
			String address = userInput.readLine(); //TODO we should probably make addresses their own class so that they can be printed neatly
			System.out.println("phone number: ");
			String phone = userInput.readLine();
			
			return "CREATE" + "\t" + userName + "\t" + 
					password + "\t" + firstName + "\t" + 
					lastName + "\t" + email + "\t" + 
					address + "\t" + phone; 
		}
		catch(IOException e){
			System.out.println("Account creation failed; please try again");
			return createAccount(userInput);
		}

	}
	
	public NewBankClient(String ip, int port) throws UnknownHostException, IOException {
		
		userInput = new BufferedReader(new InputStreamReader(System.in));
		
		// Display the welcome details
		String initialDetails = initialSetup(userInput);

		server = new Socket(ip,port); 
		bankServerOut = new PrintWriter(server.getOutputStream(), true); 
		BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream()));

		//Send initial details
		bankServerOut.println(initialDetails);
		
		bankServerResponceThread = new Thread() {
			 
			public void run() {
				// Ask for login details of the user
				//bankServerOut.println(initialDetails);
				
				try {
					while(true) {
						String response = bankServerIn.readLine();
						// Code to prevent a null error if the user isn't logged in
						
						if (response == null || response == "exit"){
							// Terminate the loop - need to use System.exit in order to 
							// kill all open threads for the client.
							System.exit(0);
						} else {

							System.out.println(response);
						}
						
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				

			}
		};
		bankServerResponceThread.start();
		
	}
	
	
	public void run() {
		// This block handles all user input from the client
		//while(true){}
			try {
				while(true) {
					String command;
					
					command = userInput.readLine();
					
					bankServerOut.println(command);
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}

	// Displays the initial welcome menu to log into the platform
	private String initialSetup(BufferedReader userInput){
		// Print the initial welcome to the screen
		System.out.println("Welcome to NewBank!");
		System.out.println("1. Login");
		System.out.println("2. Create account");
		System.out.println("3. Exit");
		System.out.println("Enter option (number)");

		// 
		try{
			String response = userInput.readLine();
			if (response.contains("1")){
				return login(userInput);
			}
			if (response.contains("2")){
				return createAccount(userInput);
			}
			if (response.contains("3")){
				// exit the thread
				return "EXIT";
			}
		}
		catch(IOException e){
			e.printStackTrace();
			
		}
		// Recursively remain on this menu until either 1 or 2 is selected
		return initialSetup(userInput);
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// Starting point of the notebook
		new NewBankClient("localhost",14002).start();
	}
}
