public class Transfer extends ATMTransaction {
    private int destinationAccountNumber;
    private int sourceAccountNumber;
    private double amount;

    public Transfer(long id, int source, int destination, double amount) {
        super(id, "TRANSFER");
        if (amount <= 0)
            throw new IllegalArgumentException("Transfer must be positive");
        sourceAccountNumber = source;
        destinationAccountNumber = destination;
        this.amount = amount;
    }

    public int getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public int getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void logicTransfer() {
    }

    public void execute(BankingService service) {
        if (!service.canWithdraw(sourceAccountNumber, amount))
            throw new IllegalArgumentException("Insufficient funds");
        modifyAccount(service);
        logicTransfer();
    }

    public void modifyAccount(BankingService service) {
        service.transfer(sourceAccountNumber, destinationAccountNumber, amount);
    }
}