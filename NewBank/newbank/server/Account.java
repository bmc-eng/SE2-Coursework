package newbank.server;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;

public class Account implements Serializable {
	
	private ArrayList<String> accountRecord;
	private String accountName;
	private double balance;
	private String Type;
	

	public Account(String accountName, double openingBalance, String type) {
		this.accountName = accountName;
		this.balance = openingBalance;
		this.Type = type;

		// Start an account record
		accountRecord = new ArrayList<String>();
		accountRecord.add(getTimeForLog() + " " + accountName + " OPENED.\t Balance: " + openingBalance);
	}

	public double getBalance(){
		return balance;
	}

	// Mutator method to move money out (debit) of the account
	public boolean addMoney(double transferAmount){
		if (transferAmount > 0){
			balance += transferAmount;
			accountRecord.add(getTimeForLog() + " " + accountName + " TRANSFER IN \t" + transferAmount + 
				" \tBalance: " + balance);
			return true;
		}
		return false;
		
	}
	// Mutator method to move money into (credit) of the account
	public boolean transferAmount(double transferAmount){
		if (transferAmount > 0){
			balance -= transferAmount;
			accountRecord.add(getTimeForLog() + "\t" + accountName + "\t TRANSFER OUT \t-" + transferAmount + 
				" \tBalance: " + balance);
			return true;
		}

		return false;
		
	}
	
	public String toString() {
		return (accountName + ": " + balance);
	}

	public String getType(){
		return this.Type;
	}

	public String getName(){
		return this.accountName;
	}

	public String getAccountStatements(){
		String statement = new String();
		for (String s: accountRecord){
			statement = statement + "\n" + s;
		}
		return statement;
	}

	private String getTimeForLog(){
		return Instant.now().toString();
	}

}
