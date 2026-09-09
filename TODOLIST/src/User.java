public class User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String username, String password) {
        if (this.username != null && this.password != null) {
            return this.username.equals(username) && this.password.equals(password);
        }
        return false;
    }
}