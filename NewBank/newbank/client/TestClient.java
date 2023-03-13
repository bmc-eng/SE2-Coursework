package newbank.client;

import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class TestClient extends Thread{
    private Socket server;
	private PrintWriter bankServerOut;	
	private Thread bankServerResponceThread;
	
	public TestClient(String ip, int port) throws UnknownHostException, IOException {
        // create connection to the server
		server = new Socket(ip,port);

        // Output the results from the server
		bankServerOut = new PrintWriter(server.getOutputStream(), true); 

		bankServerResponceThread = new Thread() {
			private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream())); 
			public void run() {

                // this is taking input from the user
				try {
					while(true) {
						String responce = bankServerIn.readLine();
						// Code to prevent a null error if the user isn't logged in
						if (responce == null || responce == "exit"){
							// Terminate the loop - need to use System.exit in order to 
							// kill all open threads for the client.
							System.exit(0);
						} else {
							System.out.println(responce);
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
        // Login Routing
        try {
            bankServerOut.println("UserAccount1");
            System.out.println("UserAccount1");
            Thread.sleep(100);
            bankServerOut.println("123");
            System.out.println("123");
            Thread.sleep(100);
            bankServerOut.println("null");
        } catch (Exception e){
            System.out.println("error");
        }
        
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new TestClient("localhost",14002).start();
	}
}
