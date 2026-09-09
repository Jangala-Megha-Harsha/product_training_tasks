import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the Customer Management System!");

        Scanner scanner = new Scanner(System.in);

        DataStore.AppData data = DataStore.load();

        if (data.admins.isEmpty()) {
            Admin defaultAdmin = new Admin();
            defaultAdmin.setaId("A001");
            defaultAdmin.setName("Main Admin");
            defaultAdmin.setUsername("admin");
            defaultAdmin.setPassword("admin123");
            data.admins.add(defaultAdmin);
            DataStore.save(data);
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Admin login");
            System.out.println("2. Customer login");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Admin loggedAdmin = loginAdmin(scanner, data);
                    if (loggedAdmin != null) {
                        adminMenu(scanner, data, loggedAdmin);
                    }
                    break;

                case 2:
                    Customer loggedCustomer = loginCustomer(scanner, data);
                    if (loggedCustomer != null) {
                        customerMenu(scanner, data, loggedCustomer);
                    }
                    break;

                case 0:
                    running = false;
                    DataStore.save(data);
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    private static Admin loginAdmin(Scanner scanner, DataStore.AppData data) {
        String username = readLine(scanner, "Enter admin username: ");
        String password = readLine(scanner, "Enter admin password: ");

        for (Admin admin : data.admins) {
            if (admin.login(username, password)) {
                System.out.println("Admin login successful.");
                return admin;
            }
        }

        System.out.println("Invalid admin credentials.");
        return null;
    }

    private static Customer loginCustomer(Scanner scanner, DataStore.AppData data) {
        String username = readLine(scanner, "Enter customer username: ");
        String password = readLine(scanner, "Enter customer password: ");

        for (Customer customer : data.customers) {
            if (customer.login(username, password)) {
                System.out.println("Customer login successful.");
                return customer;
            }
        }

        System.out.println("Invalid customer credentials.");
        return null;
    }

    private static void adminMenu(Scanner scanner, DataStore.AppData data, Admin admin) {
        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println();
            System.out.println("Admin Menu");
            System.out.println("1. Add customer");
            System.out.println("2. Edit customer");
            System.out.println("3. Delete customer");
            System.out.println("4. View customer");
            System.out.println("5. View all customers");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Customer customer = new Customer();
                    customer.setCid(readLine(scanner, "Enter customer id: "));
                    customer.setCname(readLine(scanner, "Enter customer name: "));
                    customer.setUsername(readLine(scanner, "Enter username: "));
                    customer.setPassword(readLine(scanner, "Enter password: "));
                    data.customers.add(customer);
                    admin.addCustomer(customer);
                    DataStore.save(data);
                    System.out.println("Customer saved.");
                    break;

                case 2:
                    Customer editCustomer = findCustomerById(data.customers, readLine(scanner, "Enter customer id to edit: "));
                    if (editCustomer == null) {
                        System.out.println("Customer not found.");
                        break;
                    }
                    System.out.println("What do you want to edit?");
                    System.out.println("1. Customer name");
                    System.out.println("2. Username");
                    System.out.println("3. Password");
                    System.out.print("Enter choice: ");
                    int editChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (editChoice) {
                        case 1:
                            editCustomer.setCname(readLine(scanner, "Enter new customer name: "));
                            break;
                        case 2:
                            editCustomer.setUsername(readLine(scanner, "Enter new username: "));
                            break;
                        case 3:
                            editCustomer.setPassword(readLine(scanner, "Enter new password: "));
                            break;
                        default:
                            System.out.println("Invalid edit choice.");
                            break;
                    }
                    admin.editCustomer(editCustomer);
                    DataStore.save(data);
                    System.out.println("Customer updated.");
                    break;

                case 3:
                    String deleteCustomerId = readLine(scanner, "Enter customer id to delete: ");
                    Customer removeCustomer = findCustomerById(data.customers, deleteCustomerId);
                    if (removeCustomer == null) {
                        System.out.println("Customer not found.");
                        break;
                    }
                    data.customers.remove(removeCustomer);
                    data.tasks.removeIf(task -> deleteCustomerId.equals(task.getCreatedBy()));
                    admin.deleteCustomer(removeCustomer);
                    DataStore.save(data);
                    System.out.println("Customer deleted.");
                    break;

                case 4:
                    Customer viewCustomer = findCustomerById(data.customers, readLine(scanner, "Enter customer id to view: "));
                    if (viewCustomer == null) {
                        System.out.println("Customer not found.");
                        break;
                    }
                    admin.viewCustomerDetails(viewCustomer);
                    break;

                case 5:
                    if (data.customers.isEmpty()) {
                        System.out.println("No customers available.");
                        break;
                    }
                    for (Customer item : data.customers) {
                        printCustomer(item);
                        System.out.println();
                    }
                    break;

                case 0:
                    adminRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void customerMenu(Scanner scanner, DataStore.AppData data, Customer loggedCustomer) {
        boolean customerRunning = true;
        while (customerRunning) {
            System.out.println();
            System.out.println("Customer Menu");
            System.out.println("1. Add task");
            System.out.println("2. Update task");
            System.out.println("3. Delete task");
            System.out.println("4. View my tasks");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Task newTask = new Task();
                    newTask.setTid(readLine(scanner, "Enter task id: "));
                    newTask.setTname(readLine(scanner, "Enter task name: "));
                    newTask.setDescription(readLine(scanner, "Enter task description: "));
                    newTask.setCreatedBy(loggedCustomer.getCid());
                    data.tasks.add(newTask);
                    loggedCustomer.addTask(newTask.getTid());
                    DataStore.save(data);
                    System.out.println("Task saved.");
                    break;

                case 2:
                    Task taskToUpdate = findOwnedTask(data.tasks, loggedCustomer.getCid(), readLine(scanner, "Enter task id to update: "));
                    if (taskToUpdate == null) {
                        System.out.println("Task not found.");
                        break;
                    }
                    taskToUpdate.setTname(readLine(scanner, "Enter new task name: "));
                    taskToUpdate.setDescription(readLine(scanner, "Enter new task description: "));
                    DataStore.save(data);
                    System.out.println("Task updated.");
                    break;

                case 3:
                    String deleteTaskId = readLine(scanner, "Enter task id to delete: ");
                    Task taskToDelete = findOwnedTask(data.tasks, loggedCustomer.getCid(), deleteTaskId);
                    if (taskToDelete == null) {
                        System.out.println("Task not found.");
                        break;
                    }
                    data.tasks.remove(taskToDelete);
                    loggedCustomer.delTask(deleteTaskId);
                    DataStore.save(data);
                    System.out.println("Task deleted.");
                    break;

                case 4:
                    printTasksForCustomer(data.tasks, loggedCustomer.getCid());
                    break;

                case 0:
                    customerRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static String readLine(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private static Customer findCustomerById(List<Customer> customers, String cid) {
        for (Customer customer : customers) {
            if (customer.getCid() != null && customer.getCid().equals(cid)) {
                return customer;
            }
        }
        return null;
    }

    private static Task findOwnedTask(List<Task> tasks, String customerId, String taskId) {
        for (Task task : tasks) {
            if (task.getTid() != null && task.getTid().equals(taskId) && customerId.equals(task.getCreatedBy())) {
                return task;
            }
        }
        return null;
    }

    private static void printCustomer(Customer customer) {
        System.out.println("Customer ID: " + customer.getCid());
        System.out.println("Customer Name: " + customer.getCname());
        System.out.println("Username: " + customer.getUsername());
        System.out.println("Tasks: " + customer.getTaskIds());
    }

    private static void printTasksForCustomer(List<Task> tasks, String customerId) {
        boolean found = false;
        for (Task task : tasks) {
            if (customerId.equals(task.getCreatedBy())) {
                found = true;
                System.out.println("Task ID: " + task.getTid());
                System.out.println("Task Name: " + task.getTname());
                System.out.println("Description: " + task.getDescription());
                System.out.println("Created By: " + task.getCreatedBy());
                System.out.println();
            }
        }
        if (!found) {
            System.out.println("No tasks found.");
        }
    }

}