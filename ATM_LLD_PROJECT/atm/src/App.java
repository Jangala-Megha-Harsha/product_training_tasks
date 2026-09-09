import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        Path dataFile = Paths.get("atm-data.json");
        BankingService bankingService = new BankingService(new JsonStorage(dataFile));
        bankingService.load();
        new ConsoleMenu(bankingService).start();
    }
}