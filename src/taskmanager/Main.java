package taskmanager;

import taskmanager.screens2.LoginScreen;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize data files before starting
        FileManager.initFiles();

        // Run on the Event Dispatch Thread (EDT) — required for Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Nimbus look and feel for a modern appearance
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // If Nimbus not available, use system default
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {}
            }

            new LoginScreen().setVisible(true);
        });
        System.out.println("\n=== Press Enter to exit ===");
        new Scanner(System.in).nextLine();
    }
}
