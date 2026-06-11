package taskmanager.models;

public class Task {
    private int id;
    private String ownerUsername;
    private String title;
    private String description;
    private String status; // PENDING or DONE
    private String date;

    public Task(int id, String ownerUsername, String title, String description, String status, String date) {
        this.id = id;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.description = description;
        this.status = status;
        this.date = date;
    }

    // Getters
    public int getId() { return id; }
    public String getOwnerUsername() { return ownerUsername; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getDate() { return date; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setDate(String date) { this.date = date; }

    // Convert to file line format: id|owner|title|description|status|date
    public String toFileLine() {
        return id + "|" + ownerUsername + "|" + title + "|" + description + "|" + status + "|" + date;
    }

    // Create Task from file line
    public static Task fromFileLine(String line) {
        String[] parts = line.split("\\|", 6);
        if (parts.length == 6) {
            return new Task(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                parts[5]
            );
        }
        return null;
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status='" + status + "'}";
    }
}
