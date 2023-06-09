package newbank.server;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
	
	private ArrayList<Account> accounts;
	private ArrayList<Loan> loans;
	private String customerEmailAddress;
	private String customerAddress;
	private String customerPhoneNumber; 
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	

	// Adding a specific constructor method for populating from Database
	public Customer(String userName, String password, String firstName, String lastName, String address, String email){
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.customerAddress = address;
		this.customerEmailAddress = email;
		// To do: add info on accounts
		accounts = new ArrayList<>();
		loans = new ArrayList<>();
		
	}

	 
	public Customer(String userName, String password){
		this.userName = userName;
		this.password = password;
		accounts = new ArrayList<>();
	}
	
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString() + "\n";
		}
		return s;
	}

	public double getCurrentBalance(){
		Account a = getCurrentAccount();
		if (a != null){
			return a.getBalance();
		} else {
			return 0.0;
		}
	}

	public Account getCurrentAccount(){
		for (Account a: accounts) {
			if (a.getType().toLowerCase().contains("current")){
				return a;
			}
		}
		return null;
	}

	public ArrayList<Account> getAccounts(){
		return this.accounts;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}

	public void addLoan(Loan loan) {
		loans.add(loan);		
	}

	public String getUsername(){
		return userName;
	}

	public String getPassword(){
		return password;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public String getEmail(){
		return customerEmailAddress; 
	}

	public String getAddress(){
		return customerAddress;
	}

	public String getPhoneNumber(){
		return customerPhoneNumber;
	}

	public void changeEmail(String newEmail){
		customerEmailAddress = newEmail;
	}

	public void changeAddress(String newAddress){
		customerAddress = newAddress; 
	}

	public void changePhoneNumber(String newPhone){
		customerPhoneNumber = newPhone; 
	}

	public void changePassword(String password){
		this.password = password;
	}

	public void printInfo(){
		System.out.println("Username: " + userName);
		System.out.println("Full Name: " + firstName + " " + lastName);
		System.out.println("Address: " + customerAddress);
		System.out.println("Phone Number: " + customerPhoneNumber);
		System.out.println("Email: " + customerEmailAddress);
		System.out.println();
		System.out.println("ACCOUNT DETAILS: ");


	}

	// Return information to the customer client
	public String getCustomerInformation(){
		String customerInfo = "Username: " + userName + "\n" +
								"Full Name: " + firstName + " " + lastName + "\n" +
								"Address: " + customerAddress + "\n" +
								"Phone Number: " + customerPhoneNumber + "\n" +
								"Email: " + customerEmailAddress + "\n\n" +
								"ACCOUNT DETAILS: \n" + accountsToString();
		return customerInfo;  
	}

	// Return a String of the statements from a particular account
	public String getStatements(String accountName){
		for (Account selectedAccount : accounts){
			if (selectedAccount.getName().contains(accountName)){
				return selectedAccount.getAccountStatements();
			}
		}
		return null;
	}

	public void updateSavings(SavingsAccount savings){
		for(Account account: accounts){
			if(account.getType().contentEquals("Savings")){
				int location = accounts.indexOf(account);
				accounts.set(location, savings);
			}
		}	
	}


	// Update whether a savings account exists for this customer
	public boolean hasSavings(){
		for(Account account: accounts){
			if(account.getType().contentEquals("Savings")){
				return true;
			}
		}
		return false;
	}

	public SavingsAccount getSavings(){
		for(Account account: accounts){
			if(account.getType().contentEquals("Savings")){
				return (SavingsAccount) account;
			}
		}
		return null;
	}


	public boolean hasLoan(){
		for(Account account: accounts){
			if(account.getType().contentEquals("Loan")){
				return true;
			}
		}
		return false;
	}

	public Loan getLoan(){
		for(Loan loan: loans){
			if(loan.getType().contentEquals("Loan")){
				return (Loan) loan;
			}
		}
		return null;
	}


	public void updateLoan(Loan loan){
		for(Account account: accounts){
			if(account.getType().contentEquals("Loan")){
				int location = accounts.indexOf(account);
				loans.set(location, loan);
			}
		}	
	}



	
}
