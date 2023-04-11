package newbank.server;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	private Database db = new Database();
	private double savingsRate = 0.05;
	
	private NewBank() {
		customers = new HashMap<>();
		db = new Database();
		
		// Needed for resetting the objects when changing the class file
		try {
			addTestData();
		} catch (IOException ioe){
			System.out.println("Error");
		}
		
		
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	// Added code for serialised users 
	public synchronized CustomerID checkLogInDetails(String userName, String password, Customer customer) {
		try{
			if (customer.getUsername().contains(userName) && customer.getPassword().contains(password)){
				return new CustomerID(userName);
			}
			return null;
		} catch (NullPointerException npe){
			return null;
		}
		
	}

	// commands from the NewBank customer are processed in this method. 
	public synchronized String processRequest(Customer customer, String request) {
		// Split the request up into different strings - this will enable multiple commands on one line
		String[] requests = request.split(" ");
		try{
			// This lists all of the possible actions of the bank
			switch(requests[0].toUpperCase()) {
				case "SHOWMYACCOUNTS": return showMyAccounts(customer);
				case "NEWCURRENT" : return addCurrentAccount(customer);
				case "NEWSAVINGS" : return addSavingsAccount(customer);
				case "NEWLOAN" : return addLoan(customer, requests);
				case "INFO" : return showFullDetails(customer);
				case "TRANSFER" : return transferToUser(customer, requests);
				case "VIEWSTATEMENT" : return viewStatementForAccount(customer, requests);
				case "EDITPROFILE" : return editProfile(customer, requests);
				case "RESETPASSWORD" : return resetPassword(customer, requests);
				case "EXIT" : return "LOGGING OFF...";
				
				
				default : return "UNABLE TO PROCESS. Your options are: \n\n" +
							"SHOWMYACCOUNTS\nNEWCURRENT\nNEWSAVINGS\nINFO\nTRANSFER\nVIEWSTATEMENT\nEDITPROFILE\nRESETPASSWORD\nEXIT";
			}
		}
		catch( Exception e){
			return e.getMessage(); // Return error message for resolving errors in development phase
			//return "FAIL";
		}
	}

	// Method to view the statement logs for a given account. Shows all transactions
	private String viewStatementForAccount(Customer customer, String[] instructions){
		// get the statement for a specific account
		String accountName = instructions[2];
		String accountStatement = customer.getStatements(accountName);
		if (accountStatement != null){
			return accountStatement;
		} else {
			return "Unable to retrieve account statements for: " + accountName;
		}
	}

	// Method to reset a customer's password
	private String resetPassword(Customer customer, String[] instructions){
		// Correct format is the new password, separated by a space and the password retyped
		String password;
		String retypedPassword;
		try{
			password = instructions[1];
		}
		catch(Exception e){
			return "Incorrect format; write new password twice separated by a space";
		}
		try{
			retypedPassword = instructions[2];
		}
		catch(Exception e){
			return "Incorrect format; write new password twice separated by a space";
		}
		// Check if password is the same
		if(password.contentEquals(retypedPassword)){
			customer.changePassword(retypedPassword);
			db.updateCustomer(customer);
			return "OK, password reset";
		}
		else{
			return "Passwords do not match";
		}
	}

	// Method to update a customer's details
	private String editProfile(Customer customer, String[] instructions){
		// Deconstruct the request
		String attributeToChange;
		String newAttribute;
		// First parameter is the attribute to be changed
		try{
			attributeToChange = instructions[1];
		}
		catch(Exception e){
			return "Valid attributes are: email, address, phone";
		}
		// Second parameter is the new value it should be changed to
		try{
			newAttribute = instructions[2];
		}
		catch(Exception e){
			return "Missing new value to change to";
		}
		if(attributeToChange.contentEquals("email")){
			customer.changeEmail(newAttribute);
		}
		if(attributeToChange.contentEquals("address")){
			customer.changeAddress(newAttribute);
		}
		if(attributeToChange.contentEquals("phone")){
			customer.changePhoneNumber(newAttribute);
		}
		db.updateCustomer(customer);
		return "OK, profile updated.";
	}

	// Method to transfer money from one account to another
	// Only support checking to checking accounts - TRANSFER 100.0 TO john
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
		Account account = new Account(customer.getFirstName()+"Current", 0, "Current");
		customer.addAccount(account);
		db.addCustomer(customer, true);
		// Need to re-serialise the object to save changes
		return "Current account added";
		
	}

	public String addSavingsAccount(Customer customer){
		// Check if customer has an existing current account
		for(Account a: customer.getAccounts()){
			if (a.getType().contentEquals("Savings")){
				return "Savings account already exists!";
			}
		}
		SavingsAccount account = new SavingsAccount(customer.getFirstName()+"Savings", 0, "Savings", savingsRate);
		customer.addAccount(account);
		db.addCustomer(customer, true);
		// Need to re-serialise the object to save changes
		return "Savings account added";
		
	}

	public String addLoan(Customer customer, String[] request){
		// Check if customer has an existing Loan
		for(Account a: customer.getAccounts()){
			if (a.getType().contentEquals("Loan")){
				return "Sorry, You Cannot Have 2 Loans";
			}
		}
		double initialAmount;
		double term;
		try {
			initialAmount = Double.parseDouble(request[1]);
			term = Double.parseDouble(request[2]);
		} catch (NumberFormatException e){
			return "Please enter a valid amount to transfer";
		}

		Loan loan = new Loan(customer.getFirstName()+"Loan", initialAmount , term , "Loan");
		customer.addLoan(loan);
		db.addCustomer(customer, true);
		// Need to re-serialise the object to save changes
		return "Loan Approved!";	
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

		// Test password: test123
		Customer test = new Customer("test", "daef4953b9783365cad6615223720506cc46c5167cd16ab500fa597aa08ff964eb24fb19687f34d7665f778fcb6c5358fc0a5b81e1662cf90f73a2671c53f991", "T.", 
							"Est", "London", "test@test.com");
		test.addAccount(new Account("Current",1000.0, "current"));

		db.addCustomer(bhagy, true);
		db.addCustomer(christina, true);
		db.addCustomer(john, true);
		db.addCustomer(test, true);
		

	}

}
