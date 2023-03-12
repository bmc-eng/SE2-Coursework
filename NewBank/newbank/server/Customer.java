package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	private String customerEmailAddress;
	private String customerAddress;
	private String customerPhoneNumber; 
	
	public Customer(String email, String address, String phone) {
		accounts = new ArrayList<>();
		customerEmailAddress = email; 
		customerAddress = address; 
		customerPhoneNumber = phone; 
	}

	public Customer(){
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
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

	public void printInfo(){
		System.out.println("Address: " + customerAddress);
		System.out.println("Phone Number: " + customerPhoneNumber);
		System.out.println("Email: " + customerEmailAddress);
	}
}
