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

	// ******************************************
	// ************* CONSTRUCTOR ****************
	// ******************************************
	public NewBankClient(String ip, int port) throws UnknownHostException, IOException {
		// Create a reader to take information from the users Terminal 
		userInput = new BufferedReader(new InputStreamReader(System.in));
		
		// Display the welcome details and receive login details or new account creation from user
		String initialDetails = initialSetup(userInput);

		// Connect to the server
		server = new Socket(ip,port); 
		bankServerOut = new PrintWriter(server.getOutputStream(), true); 
		BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream()));

		//Send initial details to try to log into the platform
		bankServerOut.println(initialDetails);
		
		// Create new thread for receiving information from the server
		bankServerResponceThread = new Thread() {
			
			// This method is called when the server sends information back to the user
			public void run() {

				try {
					while(true) {
						// Receive a response as a string from the server
						String response = bankServerIn.readLine();
						
						// Code to prevent a null error if the user isn't logged in
						if (response == null || response == "exit"){
							// Terminate the loop - need to use System.exit in order to 
							// kill all open threads for the client.
							System.exit(0);
						} else {
							// Print the response from the server
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
	
	// This block sends user input to the server
	public void run() {
		// Called when waiting for input from the user
		try {
			while(true) {
				// Receives input from the user Terminal and send to the Server
				String command = userInput.readLine();
				// Send the command to the server
				bankServerOut.println(command);
			}				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// Displays the initial welcome menu to log into the platform
	private String initialSetup(BufferedReader userInput){
		// Print the initial welcome to the screen
		System.out.println("Welcome to NewBank!");
		System.out.println("1. Login");
		System.out.println("2. Create account");
		System.out.println("3. Exit");
		System.out.println("Enter option (number)");

		// Get the input from the user
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

	// Method to try to log into the bank -> returns a string containing the username and hashed password
	private String login(BufferedReader userInput){
		// Take user input from the client terminal
		try{
			System.out.println("Enter Username:");
			String userName = userInput.readLine();
			
			// ask for password - use Console to mask the password from screen
			System.out.println("Enter Password (Note Password is masked):");
			String password = passwordInput();

			// Convert to dictionary - Need to implement JSON method
			HashMap<String, String> loginDetails = new HashMap<String, String>();
			loginDetails.put("userName", userName);
			loginDetails.put("password", password);

			// TODO: Convert to JSON format for sending better object model to sever
			return "LOGIN" + "\t" + userName + "\t" + password;
		} catch (IOException ioe){
			return "Error";
		}

	}

	// Method to create a new account
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

	// Helper Method to hide the password input when typing on the screen
	private String passwordInput(){
		Console console = System.console();
		char[] passwordArray = console.readPassword();
		return new String (passwordArray);
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// Starting point of the notebook
		new NewBankClient("localhost",14002).start();
	}
}
