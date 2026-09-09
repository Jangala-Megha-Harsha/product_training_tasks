public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(int number, double balance, String holder, double interestRate) {
        super(number, balance, holder);
        if (interestRate < 0)
            throw new IllegalArgumentException("Invalid interest rate");
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void applyInterest() {
        updateBalance(getBalance() + getBalance() * interestRate / 100);
    }

    @Override
    public String getType() {
        return "SAVINGS";
    }
}