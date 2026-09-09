public class BalanceInquiry extends ATMTransaction {
    private int accountNumber;

    public BalanceInquiry(long id, int accountNumber) {
        super(id, "BALANCE_INQUIRY");
        this.accountNumber = accountNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance(BankingService service) {
        return service.getAccount(accountNumber).getBalance();
    }

    public void execute(BankingService service) {
        System.out.printf("Balance: %.2f%n", getBalance(service));
    }

    public void modifyAccount(BankingService service) {
    }
}