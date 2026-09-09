public class Admin extends User {
    private String aId;
    private String name;
    
    public String getaId() {
        return aId;
    }
    public void setaId(String aId) {
        this.aId = aId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void addCustomer(Customer customer) {
        System.out.println("Adding customer: " + customer.getCname());
    }

    public void editCustomer(Customer customer) {
        System.out.println("Editing customer: " + customer.getCname());
    }

    public void deleteCustomer(Customer customer) {
        System.out.println("Deleting customer: " + customer.getCname());
    }
    
    public void viewCustomerDetails(Customer customer) {
        System.out.println("Customer ID: " + customer.getCid());
        System.out.println("Customer Name: " + customer.getCname());
        System.out.println("Username: " + customer.getUsername());
        System.out.println("Tasks: " + customer.getTaskIds().size());
    }
}