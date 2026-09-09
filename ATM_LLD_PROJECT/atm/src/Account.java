public class Account {
    private int accountNumber;
    private double balance;
    private String accountHolder;

    public Account(int accountNumber, double balance, String accountHolder) {
        if (balance < 0)
            throw new IllegalArgumentException("Balance cannot be negative");
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountHolder = accountHolder;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void updateBalance(double newBalance) {
        if (newBalance < 0)
            throw new IllegalArgumentException("Balance cannot be negative");
        balance = newBalance;
    }

    public String getType() {
        return "ACCOUNT";
    }
}