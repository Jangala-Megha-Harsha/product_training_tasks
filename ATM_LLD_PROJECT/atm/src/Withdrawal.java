public class Withdrawal extends ATMTransaction {
    private double amount;
    private int accountNumber;

    public Withdrawal(long id, int accountNumber, double amount) {
        super(id, "WITHDRAWAL");
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public boolean checkFunds(BankingService service) {
        return service.canWithdraw(accountNumber, amount);
    }

    public void dispenseCash() {
        System.out.printf("Cash dispensed: %.2f%n", amount);
    }

    public void execute(BankingService service) {
        if (!checkFunds(service))
            throw new IllegalArgumentException("Insufficient funds or invalid amount");
        modifyAccount(service);
        dispenseCash();
    }

    public void modifyAccount(BankingService service) {
        service.withdraw(accountNumber, amount);
    }
}