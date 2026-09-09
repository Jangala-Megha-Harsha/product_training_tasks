import java.util.Scanner;

public class ConsoleMenu {
    private final BankingService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(BankingService service) {
        this.service = service;
    }

    public void start() {
        System.out.println("=== ATM and Banking System ===\nDemo card: 5555444433332222 | PIN: 1234");
        System.out.print("Card number (0 to exit): ");
        String number = scanner.nextLine().trim();
        if (number.equals("0"))
            return;
        DebitCard card = service.findCard(number);
        if (card == null) {
            System.out.println("Card not recognized.");
            return;
        }
        System.out.print("PIN: ");
        if (!card.validateAccess(scanner.nextLine().trim())) {
            System.out.println("Invalid PIN.");
            return;
        }
        System.out.println("Welcome, " + service.findCustomer(number).getName());
        while (true) {
            System.out.println("\n1 Balance  2 Deposit  3 Withdraw  4 Transfer  5 Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            try {
                if (choice.equals("1"))
                    run(new BalanceInquiry(nextId(), card.linksToAccount()));
                else if (choice.equals("2"))
                    run(new Deposit(nextId(), card.linksToAccount(), amount("Deposit amount: ")));
                else if (choice.equals("3"))
                    run(new Withdrawal(nextId(), card.linksToAccount(), amount("Withdrawal amount: ")));
                else if (choice.equals("4")) {
                    System.out.print("Destination account: ");
                    int destination = Integer.parseInt(scanner.nextLine());
                    run(new Transfer(nextId(), card.linksToAccount(), destination, amount("Transfer amount: ")));
                } else if (choice.equals("5"))
                    break;
                else
                    System.out.println("Invalid choice.");
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void run(ATMTransaction transaction) {
        transaction.execute(service);
        service.record(transaction);
    }

    private double amount(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine());
    }

    private long nextId() {
        return System.currentTimeMillis();
    }
}