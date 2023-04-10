package newbank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.Console;

// *********************************** THIS IS A LEGACY FILE ***********************************
// *********************************************************************************************

public class ExampleClient extends Thread{
	
	private Socket server;
	private PrintWriter bankServerOut;	
	private BufferedReader userInput;
	private Thread bankServerResponceThread;

	// Variable for determining if the client will need to hide the password input
	private AtomicBoolean isPasswordInputExpected;
	
	public ExampleClient(String ip, int port) throws UnknownHostException, IOException {
		server = new Socket(ip,port);
		userInput = new BufferedReader(new InputStreamReader(System.in)); 
		bankServerOut = new PrintWriter(server.getOutputStream(), true); 

		isPasswordInputExpected = new AtomicBoolean(false);
		
		bankServerResponceThread = new Thread() {
			private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream())); 
			public void run() {
				try {
					while(true) {
						String response = bankServerIn.readLine();
						// Code to prevent a null error if the user isn't logged in
						
						if (response == null || response == "exit"){
							// Terminate the loop - need to use System.exit in order to 
							// kill all open threads for the client.
							System.exit(0);
						} else {

							// Set up the client to input a password
							String responseString = response.toString();
							
							// Need to set this up before enter password is displayed
							if (responseString.contains("Enter Username")){
								isPasswordInputExpected.set(true);
							} else {
								isPasswordInputExpected.set(false);
							}
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
					if (isPasswordInputExpected.get()){
						// Expecting to get a password from the user
						Console console = System.console();
						char[] passwordArray = console.readPassword();
						command = new String (passwordArray);
						isPasswordInputExpected.set(false);
					} else {
						command = userInput.readLine();
					}
					
					bankServerOut.println(command);
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new ExampleClient("localhost",14002).start();
	}
}
