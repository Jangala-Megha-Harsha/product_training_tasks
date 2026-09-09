import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataStore {
    private static final Path DATA_FILE = Paths.get("data.json");

    public static class AppData {
        public final List<Admin> admins = new ArrayList<>();
        public final List<Customer> customers = new ArrayList<>();
        public final List<Task> tasks = new ArrayList<>();
    }

    public static AppData load() {
        AppData data = new AppData();

        if (!Files.exists(DATA_FILE)) {
            return data;
        }

        try {
            String json = Files.readString(DATA_FILE, StandardCharsets.UTF_8);
            data.admins.addAll(parseAdmins(json));
            data.customers.addAll(parseCustomers(json));
            data.tasks.addAll(parseTasks(json));
        } catch (IOException ignored) {
        }

        return data;
    }

    public static void save(AppData data) {
        String json = toJson(data);
        try {
            Files.writeString(DATA_FILE, json, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private static String toJson(AppData data) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"admins\": ").append(adminsToJson(data.admins)).append(",\n");
        builder.append("  \"customers\": ").append(customersToJson(data.customers)).append(",\n");
        builder.append("  \"tasks\": ").append(tasksToJson(data.tasks)).append("\n");
        builder.append("}");
        return builder.toString();
    }

    private static String adminsToJson(List<Admin> admins) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < admins.size(); i++) {
            Admin admin = admins.get(i);
            if (i > 0) {
                builder.append(",");
            }
            builder.append("{")
                    .append(jsonField("aid", admin.getaId())).append(",")
                    .append(jsonField("name", admin.getName())).append(",")
                    .append(jsonField("username", admin.getUsername())).append(",")
                    .append(jsonField("password", admin.getPassword()))
                    .append("}");
        }
        builder.append("]");
        return builder.toString();
    }

    private static String customersToJson(List<Customer> customers) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (i > 0) {
                builder.append(",");
            }
            builder.append("{")
                    .append(jsonField("cid", customer.getCid())).append(",")
                    .append(jsonField("cname", customer.getCname())).append(",")
                    .append(jsonField("username", customer.getUsername())).append(",")
                    .append(jsonField("password", customer.getPassword())).append(",")
                    .append("\"tasks\": ").append(taskRefsToJson(customer.getTaskIds()))
                    .append("}");
        }
        builder.append("]");
        return builder.toString();
    }

    private static String tasksToJson(List<Task> tasks) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (i > 0) {
                builder.append(",");
            }
            builder.append("{")
                    .append(jsonField("tid", task.getTid())).append(",")
                    .append(jsonField("tname", task.getTname())).append(",")
                    .append(jsonField("description", task.getDescription())).append(",")
                    .append(jsonField("createdBy", task.getCreatedBy()))
                    .append("}");
        }
        builder.append("]");
        return builder.toString();
    }

    private static String taskRefsToJson(List<String> taskIds) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < taskIds.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(quote(taskIds.get(i)));
        }
        builder.append("]");
        return builder.toString();
    }

    private static String jsonField(String key, String value) {
        return quote(key) + ": " + quote(value);
    }

    private static String quote(String value) {
        return "\"" + escape(value) + "\"";
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static List<Admin> parseAdmins(String json) {
        List<Admin> admins = new ArrayList<>();
        for (String block : extractObjects(json, "admins")) {
            Admin admin = new Admin();
            admin.setaId(getValue(block, "aid"));
            admin.setName(getValue(block, "name"));
            admin.setUsername(getValue(block, "username"));
            admin.setPassword(getValue(block, "password"));
            admins.add(admin);
        }
        return admins;
    }

    private static List<Customer> parseCustomers(String json) {
        List<Customer> customers = new ArrayList<>();
        for (String block : extractObjects(json, "customers")) {
            Customer customer = new Customer();
            customer.setCid(getValue(block, "cid"));
            customer.setCname(getValue(block, "cname"));
            customer.setUsername(getValue(block, "username"));
            customer.setPassword(getValue(block, "password"));
            for (String taskId : getArrayValues(block, "tasks")) {
                customer.addTask(taskId);
            }
            customers.add(customer);
        }
        return customers;
    }

    private static List<Task> parseTasks(String json) {
        List<Task> tasks = new ArrayList<>();
        for (String block : extractObjects(json, "tasks")) {
            Task task = new Task();
            task.setTid(getValue(block, "tid"));
            task.setTname(getValue(block, "tname"));
            task.setDescription(getValue(block, "description"));
            task.setCreatedBy(getValue(block, "createdBy"));
            tasks.add(task);
        }
        return tasks;
    }

    private static List<String> extractObjects(String json, String arrayName) {
        String array = extractArray(json, arrayName);
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;
        for (int i = 0; i < array.length(); i++) {
            char ch = array.charAt(i);
            if (ch == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    objects.add(array.substring(start, i + 1));
                }
            }
        }
        return objects;
    }

    private static String extractArray(String json, String arrayName) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(arrayName) + "\\\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String getValue(String json, String key) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\\"(.*?)\\\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return unescape(matcher.group(1));
        }
        return "";
    }

    private static List<String> getArrayValues(String json, String key) {
        List<String> values = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String array = matcher.group(1);
            Pattern valuePattern = Pattern.compile("\\\"(.*?)\\\"");
            Matcher valueMatcher = valuePattern.matcher(array);
            while (valueMatcher.find()) {
                values.add(unescape(valueMatcher.group(1)));
            }
        }
        return values;
    }

    private static String unescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}