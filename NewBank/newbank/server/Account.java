package newbank.server;

import java.io.Serializable;

public class Account implements Serializable {
	
	private String accountName;
	private double openingBalance;
	private String type;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public String getType(){
		return this.type;
	}

	

}
