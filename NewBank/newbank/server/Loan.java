package newbank.server;

public class Loan {
    private double rate;
    private double initialAmount;
    private double currentBalance;
    private int term;
    private int paymentsMade = 0;
    private double interestAccumulated = 0;

    public Loan(double amount, double rate, int term){
        this.initialAmount = amount;
        this.currentBalance = amount;
        this.rate = rate;
        this.term = term;
    }

    public int paymentsRemaining(){
        return term-paymentsMade;
    }

    public double currentBalance(){
        return this.currentBalance;
    }

    public void makePayment(double amount){
        currentBalance -= amount;
    }

    public void generateInterest(){
        double interest = currentBalance*rate*(1/365);
        currentBalance += interest;
        interestAccumulated += interest;
    }

    public double getInitialAmount(){
        return this.initialAmount;
    }

    public double getInterestAccumulated(){
        return this.interestAccumulated;
    }
}
