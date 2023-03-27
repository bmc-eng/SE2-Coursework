package newbank.server;

import java.io.Serializable;

public class Account implements Serializable {
	
	private String accountName;
	private double Balance;
	private String Type;

	public Account(String accountName, double openingBalance, String type) {
		this.accountName = accountName;
		this.Balance = openingBalance;
		this.Type = type;
	}

	public double getBalance(){
		return Balance;
	}

	// Mutator method to move money out (debit) of the account
	public boolean addMoney(double transferAmount){
		if (transferAmount > 0){
			Balance += transferAmount;
			return true;
		}
		return false;
		
	}
	// Mutator method to move money into (credit) of the account
	public boolean transferAmount(double transferAmount){
		Balance -= transferAmount;
		return true;
	}
	
	public String toString() {
		return (accountName + ": " + Balance);
	}

	public String getType(){
		return this.Type;
	}

}
