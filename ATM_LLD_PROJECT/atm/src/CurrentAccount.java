public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(int number, double balance, String holder, double overdraftLimit) {
        super(number, balance, holder);
        if (overdraftLimit < 0)
            throw new IllegalArgumentException("Invalid overdraft limit");
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public boolean checkOverdraft(double amount) {
        return amount >= 0 && getBalance() - amount >= -overdraftLimit;
    }

    public void withdraw(double amount) {
        if (!checkOverdraft(amount))
            throw new IllegalArgumentException("Overdraft limit exceeded");
        updateStoredBalance(getBalance() - amount);
    }

    private void updateStoredBalance(double value) {
        if (value >= 0)
            super.updateBalance(value);
        else {
            double credit = getBalance();
            super.updateBalance(0);
            negativeBalance = value;
        }
    }

    private double negativeBalance = 0;

    @Override
    public double getBalance() {
        return super.getBalance() - negativeBalance;
    }

    @Override
    public void updateBalance(double value) {
        if (value < -overdraftLimit)
            throw new IllegalArgumentException("Overdraft limit exceeded");
        negativeBalance = value < 0 ? -value : 0;
        super.updateBalance(Math.max(0, value));
    }

    @Override
    public String getType() {
        return "CURRENT";
    }
}