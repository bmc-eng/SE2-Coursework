package newbank.server;

public class SavingsAccount extends Account {
    private double savingsRate;
    private double earnedInterest = 0;
    public SavingsAccount(String accountName, double openingBalance, String type, double rate){
        super(accountName,openingBalance,type);
        this.savingsRate = rate;
    }

    // Method to calculate interest
    public void calculateInterest(){
        earnedInterest += getBalance()*savingsRate*(1/365); 
    }

    public double getEarnedInterest(){
        return this.earnedInterest;
    }
}
