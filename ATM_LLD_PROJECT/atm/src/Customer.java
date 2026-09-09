import java.util.Date;

public class Customer {
    private int customerId;
    private String name;
    private Date dob;
    private String cardNumber;

    public Customer(int customerId, String name, Date dob, String cardNumber) {
        this.customerId = customerId;
        this.name = name;
        this.dob = dob;
        this.cardNumber = cardNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void insertCard() {
    }

    public boolean enterPIN(String pin, DebitCard card) {
        return card.validateAccess(pin);
    }
}