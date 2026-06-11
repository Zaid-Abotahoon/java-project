package taskmanager.models;

public class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    // Convert to file line format: username|password|fullName
    public String toFileLine() {
        return username + "|" + password + "|" + fullName;
    }

    // Create User from file line
    public static User fromFileLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 3) {
            return new User(parts[0], parts[1], parts[2]);
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', fullName='" + fullName + "'}";
    }
}
