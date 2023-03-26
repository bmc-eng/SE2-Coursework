package newbank.server;
import java.io.IOException;
import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	private Database db;
	
	private NewBank() {
		customers = new HashMap<>();
		db = new Database();
		
		// NOT NEEDED
		/* 
		try {
			addTestData();
		} catch (IOException ioe){
			System.out.println("Error");
		}
		*/
		
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
	}

	// commands from the NewBank customer are processed in this method. 
	public synchronized String processRequest(Customer customer, String request) {
		// Split the request up into different strings - this will enable multiple commands on one line
		String[] requests = request.split(" ");
		try{
			switch(requests[0]) {
				case "SHOWMYACCOUNTS": return showMyAccounts(customer);
				case "NEWCURRENT" : return addCurrentAccount(customer);
				case "INFO" : return showFullDetails(customer);
				case "TRANSFER" : return transferToUser(customer, requests);
				
				
				default : return "UNABLE TO PROCESS. Your options are \nSHOWMYACCOUNTS\nNEWCURRENT\nINFO";
		}
		}
		catch( Exception e){
			return "FAIL";
		}
	}

	// Method to transfer money from one account to another
	// Only support checking to checking accounts - TRANSFER 100.0 john
	private String transferToUser(Customer customer, String[] instructions) {
		// Deconstruct the request
		double amountToTransfer;
		Customer userToTransfer;

		try {
			amountToTransfer = Double.parseDouble(instructions[1]);
		} catch (NumberFormatException e){
			return "Please enter a valid amount to transfer";
		}

		// Get the customer object of the person to transfer money to
		userToTransfer = db.getCustomer(instructions[3], true);
		if (userToTransfer == null){
			return "Unknown recipient of transfer - check username and try again";
		}
		
		// Check that the customer has enough money to transfer
		if (customer.getCurrentBalance() >= amountToTransfer){
			// Transfer can start to proceed

			// Step 1: Remove the money from the transfer's account
			boolean hasLeftCurrent = customer.getCurrentAccount().transferAmount(amountToTransfer);

			// Step 2: add this into the transferees current account
			boolean hasTransfered;
			if (userToTransfer.getCurrentAccount() == null){
				userToTransfer.addAccount(new Account("current",0.0, "current"));
				hasTransfered = userToTransfer.getCurrentAccount().addMoney(amountToTransfer);
			} else {
				hasTransfered = userToTransfer.getCurrentAccount().addMoney(amountToTransfer);
			}

			// Step 3: Update the records
			if (hasLeftCurrent && hasTransfered){
				if (db.updateCustomer(customer) && db.updateCustomer(userToTransfer)){
					return "Successfully transferred " + amountToTransfer + " to " + userToTransfer.getUsername();
				} else {
					// Reverse transaction
					userToTransfer.getCurrentAccount().transferAmount(amountToTransfer);
					customer.getCurrentAccount().addMoney(amountToTransfer);
					return "Transaction unsuccessful - DB error";
				}
					
			} else {
				return "Transaction unsuccessful - Problem transferring";
			}

		} else {
			return "Insufficient funds to complete transfer";
		}

	}

	private String showFullDetails(Customer customer) {
		return customer.getCustomerInformation();
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
		Account account = new Account(customer.getFirstName()+"Current", 0, "current");
		customer.addAccount(account);
		db.addCustomer(customer, true);
		// Need to re-serialise the object to save changes
		return "Current account added";

	}

	public void addNewCustomer(Customer newCustomer, String key){
		customers.put(key, newCustomer);
	}

	// ***************************************************************
	// ******* INITIAL SET UP Method - not needed once accounts set up
	// ***************************************************************
	private void addTestData() throws IOException {
		System.out.println("Setting up...");
		Customer bhagy = new Customer("bhagy", " ");
		bhagy.addAccount(new Account("main", 1000.0, "current"));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer("christina", " ");
		christina.addAccount(new Account("Savings", 1500.0, "savings"));
		customers.put("Christina", christina);
		
		Customer john = new Customer("john", " ");
		john.addAccount(new Account("checking", 250.0, "current"));
		customers.put("John", john);

		Customer test = new Customer("test", "test123", "T.", 
							"Est", "London", "test@test.com");
		test.addAccount(new Account("Current",1000.0, "current"));

		db.addCustomer(bhagy, true);
		db.addCustomer(christina, true);
		db.addCustomer(john, true);
		db.addCustomer(test, true);
		

	}

}
