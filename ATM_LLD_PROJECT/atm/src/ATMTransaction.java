import java.util.Date;

public abstract class ATMTransaction {
    private long transactionID;
    private Date dateTime;
    private String type;

    protected ATMTransaction(long id, String type) {
        transactionID = id;
        dateTime = new Date();
        this.type = type;
    }

    public long getTransactionID() {
        return transactionID;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }

    public abstract void execute(BankingService service);

    public abstract void modifyAccount(BankingService service);
}