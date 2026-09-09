public class ATM {
    private String ATMID;
    private String managedByBankID;

    public ATM(String ATMID, String managedByBankID) {
        this.ATMID = ATMID;
        this.managedByBankID = managedByBankID;
    }

    public String getATMID() {
        return ATMID;
    }

    public String getManagedByBankID() {
        return managedByBankID;
    }

    public DebitCard identifyCard(String cardNumber, BankingService service) {
        return service.findCard(cardNumber);
    }

    public void processTransaction(ATMTransaction transaction, BankingService service) {
        transaction.execute(service);
    }
}