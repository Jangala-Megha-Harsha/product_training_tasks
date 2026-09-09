import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankingService {
    private final JsonStorage storage;
    private final Map<Integer, Account> accounts = new HashMap<>();
    private final Map<String, DebitCard> cards = new HashMap<>();
    private final Map<String, Customer> customers = new HashMap<>();
    private final List<ATMTransaction> transactions = new ArrayList<>();
    private final List<Bank> banks = new ArrayList<>();
    private final List<ATM> atms = new ArrayList<>();

    public BankingService(JsonStorage storage) {
        this.storage = storage;
    }

    public void load() {
        String json = storage.read();
        for (String o : storage.objects(json, "accounts")) {
            int n = Integer.parseInt(JsonStorage.value(o, "accountNumber"));
            double b = Double.parseDouble(JsonStorage.value(o, "balance"));
            String h = JsonStorage.value(o, "accountHolder");
            String t = JsonStorage.value(o, "type");
            accounts.put(n, t.equals("SAVINGS")
                    ? new SavingsAccount(n, b, h, Double.parseDouble(JsonStorage.value(o, "interestRate")))
                    : t.equals("CURRENT")
                            ? new CurrentAccount(n, b, h, Double.parseDouble(JsonStorage.value(o, "overdraftLimit")))
                            : new Account(n, b, h));
        }
        for (String o : storage.objects(json, "cards"))
            cards.put(JsonStorage.value(o, "cardNumber"),
                    new DebitCard(JsonStorage.value(o, "cardNumber"), JsonStorage.value(o, "encryptedPIN"),
                            Integer.parseInt(JsonStorage.value(o, "linkedAccountNumber"))));
        for (String o : storage.objects(json, "customers"))
            customers.put(JsonStorage.value(o, "cardNumber"),
                    new Customer(Integer.parseInt(JsonStorage.value(o, "customerId")), JsonStorage.value(o, "name"),
                            new Date(Long.parseLong(JsonStorage.value(o, "dob"))), JsonStorage.value(o, "cardNumber")));
        if (accounts.isEmpty())
            seed();
    }

    private void seed() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1990, Calendar.JANUARY, 1);
        banks.add(new Bank("BANK-001", "Main Street"));
        atms.add(new ATM("ATM-001", "BANK-001"));
        accounts.put(1001, new SavingsAccount(1001, 1000, "Demo Customer", 2));
        cards.put("5555444433332222", new DebitCard("5555444433332222", PasswordUtil.hash("1234"), 1001));
        customers.put("5555444433332222", new Customer(1, "Demo Customer", calendar.getTime(), "5555444433332222"));
        storage.save(this);
    }

    public DebitCard findCard(String number) {
        return cards.get(number);
    }

    public Customer findCustomer(String card) {
        return customers.get(card);
    }

    public Account getAccount(int number) {
        Account a = accounts.get(number);
        if (a == null)
            throw new IllegalArgumentException("Account not found");
        return a;
    }

    public boolean canWithdraw(int number, double amount) {
        Account a = accounts.get(number);
        return amount > 0 && a != null && (a instanceof CurrentAccount ? ((CurrentAccount) a).checkOverdraft(amount)
                : a.getBalance() >= amount);
    }

    public void withdraw(int number, double amount) {
        if (!canWithdraw(number, amount))
            throw new IllegalArgumentException("Insufficient funds");
        Account a = getAccount(number);
        if (a instanceof CurrentAccount)
            ((CurrentAccount) a).withdraw(amount);
        else
            a.updateBalance(a.getBalance() - amount);
    }

    public void deposit(int number, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit must be positive");
        Account a = getAccount(number);
        a.updateBalance(a.getBalance() + amount);
    }

    public void transfer(int source, int destination, double amount) {
        if (source == destination || accounts.get(destination) == null)
            throw new IllegalArgumentException("Invalid destination account");
        withdraw(source, amount);
        try {
            deposit(destination, amount);
        } catch (RuntimeException e) {
            deposit(source, amount);
            throw e;
        }
    }

    public void record(ATMTransaction transaction) {
        transactions.add(transaction);
        storage.save(this);
    }

    public Collection<Account> accounts() {
        return accounts.values();
    }

    public Collection<DebitCard> cards() {
        return cards.values();
    }

    public Collection<Customer> customers() {
        return customers.values();
    }

    public Collection<Bank> banks() {
        return banks;
    }

    public Collection<ATM> atms() {
        return atms;
    }

    public Collection<ATMTransaction> transactions() {
        return transactions;
    }
}