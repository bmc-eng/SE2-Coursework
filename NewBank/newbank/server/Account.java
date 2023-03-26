package newbank.server;

import java.io.Serializable;

public class Account implements Serializable {
	
	private String accountName;
	private double Balance;
	private String type;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.Balance = openingBalance;

	}
	
	public String toString() {
		return (accountName + ": " + Balance);
	}

	public String getType(){
		return this.type;
	}

	public Double getBalance(){
		return this.Balance;
	}

	// Mutator method to move money out (debit) of the account
	public void Debit(double debitAmount){
		this.Balance -= debitAmount;
	}

	// Mutator method to move money into (credit) of the account
	public void Credit(double creditAmount){
		this.Balance += creditAmount;
	}

}
