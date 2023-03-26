package newbank.server;

import java.io.Serializable;

public class Account implements Serializable {
	
	private String accountName;
	private double openingBalance;
	private String type;

	public Account(String accountName, double openingBalance, String type) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
		this.type = type;
	}

	public double getBalance(){
		return openingBalance;
	}

	public boolean addMoney(double transferAmount){
		if (transferAmount > 0){
			openingBalance += transferAmount;
			return true;
		}
		return false;
		
	}

	public boolean transferAmount(double transferAmount){
		openingBalance -= transferAmount;
		return true;
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public String getType(){
		return this.type;
	}



}
