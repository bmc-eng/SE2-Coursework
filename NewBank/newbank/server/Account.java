package newbank.server;

import java.io.Serializable;

public class Account implements Serializable {
	
	private String accountName;
	private double Balance;
	private String Type;

	public Account(String accountName, double openingBalance, String Type) {
		this.accountName = accountName;
		this.Balance = openingBalance;
		this.Type = Type;
	}
	
	public String toString() {
		return (accountName + ": " + Balance);
	}

	public String getType(){
		return this.Type;
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
