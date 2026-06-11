package taskmanager;

import taskmanager.models.Task;
import taskmanager.models.User;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    // ===================== FILE PATHS =====================
    private static final String USERS_FILE  = "data/users.txt";
    private static final String TASKS_FILE  = "data/tasks.txt";

    // ===================== CORE FILE METHODS (المعطاة من الدكتور) =====================

    public static String readFile(String fileName) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line + "\n";
        }
        br.close();
        return result;
    }

    public static void writeToFile(String fileName, String msg, boolean append) throws IOException {
        FileWriter fw = new FileWriter(fileName, append);
        fw.append(msg);
        fw.close();
    }

    // ===================== INITIALIZATION =====================

    public static void initFiles() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdirs();

            File usersFile = new File(USERS_FILE);
            if (!usersFile.exists()) {
                usersFile.createNewFile();
                writeToFile(USERS_FILE, "admin|admin123|Admin User\n", false);
            }

            File tasksFile = new File(TASKS_FILE);
            if (!tasksFile.exists()) {
                tasksFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error initializing files: " + e.getMessage());
        }
    }

    // ===================== USER OPERATIONS =====================

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String content = readFile(USERS_FILE);
            String[] lines = content.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty()) {
                    User user = User.fromFileLine(line);
                    if (user != null) users.add(user);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet — return empty list
        }
        return users;
    }

    public static boolean userExists(String username) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equalsIgnoreCase(username)) return true;
        }
        return false;
    }

    public static User validateLogin(String username, String password) {
        for (User u : getAllUsers()) {
            if (u.getUsername().equalsIgnoreCase(username) &&
                u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static boolean registerUser(User newUser) {
        if (userExists(newUser.getUsername())) return false;
        try {
            writeToFile(USERS_FILE, newUser.toFileLine() + "\n", true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ===================== TASK OPERATIONS =====================

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            String content = readFile(TASKS_FILE);
            String[] lines = content.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Task task = Task.fromFileLine(line);
                    if (task != null) tasks.add(task);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet — return empty list
        }
        return tasks;
    }

    public static List<Task> getTasksForUser(String username) {
        List<Task> userTasks = new ArrayList<>();
        for (Task t : getAllTasks()) {
            if (t.getOwnerUsername().equalsIgnoreCase(username)) {
                userTasks.add(t);
            }
        }
        return userTasks;
    }

    private static int getNextTaskId() {
        List<Task> all = getAllTasks();
        if (all.isEmpty()) return 1;
        int maxId = 0;
        for (Task t : all) {
            if (t.getId() > maxId) maxId = t.getId();
        }
        return maxId + 1;
    }

    public static boolean addTask(String ownerUsername, String title, String description, String status) {
        try {
            int newId = getNextTaskId();
            String today = LocalDate.now().toString();
            Task task = new Task(newId, ownerUsername, title, description, status, today);
            writeToFile(TASKS_FILE, task.toFileLine() + "\n", true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Rewrite entire tasks file without the deleted task
    public static boolean deleteTask(int taskId) {
        List<Task> all = getAllTasks();
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        for (Task t : all) {
            if (t.getId() == taskId) {
                found = true; // skip this one
            } else {
                sb.append(t.toFileLine()).append("\n");
            }
        }
        if (!found) return false;
        try {
            writeToFile(TASKS_FILE, sb.toString(), false);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Toggle PENDING <-> DONE
    public static boolean updateTaskStatus(int taskId, String newStatus) {
        List<Task> all = getAllTasks();
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        for (Task t : all) {
            if (t.getId() == taskId) {
                t.setStatus(newStatus);
                found = true;
            }
            sb.append(t.toFileLine()).append("\n");
        }
        if (!found) return false;
        try {
            writeToFile(TASKS_FILE, sb.toString(), false);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Count helpers for Dashboard
    public static int countPending(String username) {
        int count = 0;
        for (Task t : getTasksForUser(username)) {
            if (t.getStatus().equals("PENDING")) count++;
        }
        return count;
    }

    public static int countDone(String username) {
        int count = 0;
        for (Task t : getTasksForUser(username)) {
            if (t.getStatus().equals("DONE")) count++;
        }
        return count;
    }
}
