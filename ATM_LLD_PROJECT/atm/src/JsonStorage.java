import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonStorage {
    private final Path file;

    public JsonStorage(Path file) {
        this.file = file;
    }

    public void save(BankingService service) {
        StringBuilder json = new StringBuilder("{\n  \"customers\":[");
        for (Customer c : service.customers())
            json.append(String.format("{\"customerId\":%d,\"name\":\"%s\",\"dob\":%d,\"cardNumber\":\"%s\"},",
                    c.getCustomerId(), esc(c.getName()), c.getDob().getTime(), esc(c.getCardNumber())));
        trimComma(json);
        json.append("],\n  \"cards\":[");
        for (DebitCard c : service.cards())
            json.append(String.format("{\"cardNumber\":\"%s\",\"encryptedPIN\":\"%s\",\"linkedAccountNumber\":%d},",
                    esc(c.getCardNumber()), c.getEncryptedPIN(), c.getLinkedAccountNumber()));
        trimComma(json);
        json.append("],\n  \"accounts\":[");
        for (Account a : service.accounts()) {
            json.append(String.format("{\"type\":\"%s\",\"accountNumber\":%d,\"balance\":%.2f,\"accountHolder\":\"%s\"",
                    a.getType(), a.getAccountNumber(), a.getBalance(), esc(a.getAccountHolder())));
            if (a instanceof SavingsAccount)
                json.append(String.format(",\"interestRate\":%.2f", ((SavingsAccount) a).getInterestRate()));
            if (a instanceof CurrentAccount)
                json.append(String.format(",\"overdraftLimit\":%.2f", ((CurrentAccount) a).getOverdraftLimit()));
            json.append("},");
        }
        trimComma(json);
        json.append("],\n  \"banks\":[");
        for (Bank b : service.banks())
            json.append(String.format("{\"bankCode\":\"%s\",\"address\":\"%s\"},", esc(b.getBankCode()),
                    esc(b.getAddress())));
        trimComma(json);
        json.append("],\n  \"atms\":[");
        for (ATM a : service.atms())
            json.append(String.format("{\"ATMID\":\"%s\",\"managedByBankID\":\"%s\"},", esc(a.getATMID()),
                    esc(a.getManagedByBankID())));
        trimComma(json);
        json.append("],\n  \"transactions\":[");
        for (ATMTransaction t : service.transactions())
            json.append(String.format("{\"transactionID\":%d,\"dateTime\":%d,\"type\":\"%s\"},", t.getTransactionID(),
                    t.getDateTime().getTime(), esc(t.getType())));
        trimComma(json);
        json.append("]\n}\n");
        try {
            Files.write(file, json.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Could not save data", e);
        }
    }

    public String read() {
        try {
            return Files.exists(file) ? Files.readString(file) : "";
        } catch (IOException e) {
            throw new IllegalStateException("Could not read data", e);
        }
    }

    public List<String> objects(String json, String array) {
        Matcher section = Pattern.compile("\\\"" + array + "\\\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL).matcher(json);
        List<String> result = new ArrayList<>();
        if (!section.find())
            return result;
        Matcher object = Pattern.compile("\\{(.*?)\\}").matcher(section.group(1));
        while (object.find())
            result.add(object.group(1));
        return result;
    }

    public static String value(String object, String key) {
        Matcher m = Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*(?:\\\"(.*?)\\\"|([^,}]+))").matcher(object);
        return m.find() ? (m.group(1) != null ? m.group(1) : m.group(2).trim()) : "";
    }

    private static void trimComma(StringBuilder b) {
        if (b.length() > 0 && b.charAt(b.length() - 1) == ',')
            b.deleteCharAt(b.length() - 1);
    }

    private static String esc(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}