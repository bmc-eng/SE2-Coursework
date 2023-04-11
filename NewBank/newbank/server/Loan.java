package newbank.server;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;

public class Loan implements Serializable{
    private ArrayList<String> LoanRecord;
    private static double rate = 0.1;
    private static double initialAmount;
    private double currentBalance;
    private static double term;
    private static double paymentsMade = 0;
    private double interestAccumulated = 0;
    private String type;

    public Loan(String customer,double initialAmount, double term, String type){
        Loan.term = term;
        LoanRecord = new ArrayList<String>();
        LoanRecord.add(getTimeForLog() + "Loan Accepted For the Ammount: " + initialAmount);
    }

    public Loan(String string, int initialAmount2, String term2, Object type2) {
    }

    public double paymentsRemaining(){
        return term-paymentsMade;
    }

    public static double currentBalance(){
        double currentBalance = ((initialAmount*(1+rate)-initialAmount)/12)+initialAmount-(paymentsMade*(initialAmount*(1+rate)/term));
        return currentBalance;
    }

    public void makePayment(double amount){
        currentBalance -= amount;
        paymentsMade = paymentsMade+1;
    }

    public void generateInterest(){
        double interest = currentBalance*rate*(1/365);
        currentBalance += interest;
        interestAccumulated += interest;
    }

    public double getInitialAmount(){
        return Loan.initialAmount;
    }

    public double getInterestAccumulated(){
        return this.interestAccumulated;
    }

    public double getInterestrate(){
        return Loan.rate;
    }

    private String getTimeForLog(){
		return Instant.now().toString();
	}

    public String getType(){
		return this.type;
	}
}
