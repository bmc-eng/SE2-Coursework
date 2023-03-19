package newbank.server;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
	
	private ArrayList<Account> accounts;
	private String customerEmailAddress;
	private String customerAddress;
	private String customerPhoneNumber; 
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	
	public Customer(String email, String address, String phone) {
		accounts = new ArrayList<>();
		customerEmailAddress = email; 
		customerAddress = address; 
		customerPhoneNumber = phone; 
	}

	// Adding a specific constructor method for populating from Database
	public Customer(String userName, String password, String firstName, String lastName, String address, String email){
		this.userName = userName;
		this.password = password;
		this.firstName =firstName;
		this.lastName = lastName;
		this.customerAddress = address;
		this.customerEmailAddress = email;
		// To do: add info on accounts
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

	public void printInfo(){
		System.out.println("Address: " + customerAddress);
		System.out.println("Phone Number: " + customerPhoneNumber);
		System.out.println("Email: " + customerEmailAddress);
	}
}
