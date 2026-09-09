public class Deposit extends ATMTransaction {
    private double depositAmount;
    private int accountNumber;

    public Deposit(long id, int accountNumber, double amount) {
        super(id, "DEPOSIT");
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit must be positive");
        this.accountNumber = accountNumber;
        depositAmount = amount;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void creditAccount() {
    }

    public void execute(BankingService service) {
        modifyAccount(service);
        creditAccount();
    }

    public void modifyAccount(BankingService service) {
        service.deposit(accountNumber, depositAmount);
    }
}