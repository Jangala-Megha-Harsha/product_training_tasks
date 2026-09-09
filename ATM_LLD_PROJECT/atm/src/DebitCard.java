public class DebitCard {
    private String cardNumber;
    private String encryptedPIN;
    private int linkedAccountNumber;

    public DebitCard(String cardNumber, String encryptedPIN, int linkedAccountNumber) {
        this.cardNumber = cardNumber;
        this.encryptedPIN = encryptedPIN;
        this.linkedAccountNumber = linkedAccountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getEncryptedPIN() {
        return encryptedPIN;
    }

    public int getLinkedAccountNumber() {
        return linkedAccountNumber;
    }

    public boolean validateAccess(String pin) {
        return PasswordUtil.hash(pin).equals(encryptedPIN);
    }

    public int linksToAccount() {
        return linkedAccountNumber;
    }
}