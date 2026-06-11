package taskmanager;

import taskmanager.models.User;

// Simple session holder — keeps current logged-in user accessible from all screens
public class Session {
    private static User currentUser = null;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getUsername() {
        return currentUser != null ? currentUser.getUsername() : "";
    }

    public static String getFullName() {
        return currentUser != null ? currentUser.getFullName() : "";
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
