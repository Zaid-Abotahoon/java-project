package taskmanager.screens2;

import taskmanager.FileManager;
import taskmanager.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardScreen extends JFrame {

    public DashboardScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Task Manager - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(30, 30, 46));

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(24, 24, 37));
        topBar.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblApp = new JLabel("📋 Task Manager");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblApp.setForeground(new Color(203, 166, 247));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(243, 139, 168));
        btnLogout.setForeground(new Color(30, 30, 46));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        topBar.add(lblApp, BorderLayout.WEST);
        topBar.add(btnLogout, BorderLayout.EAST);

        // ===== WELCOME SECTION =====
        JPanel welcomePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        welcomePanel.setBackground(new Color(30, 30, 46));
        welcomePanel.setBorder(new EmptyBorder(25, 30, 10, 30));

        JLabel lblWelcome = new JLabel("Welcome back, " + Session.getFullName() + "! 👋");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(new Color(205, 214, 244));

        JLabel lblSub = new JLabel("Here's an overview of your tasks");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(166, 173, 200));

        welcomePanel.add(lblWelcome);
        welcomePanel.add(lblSub);

        // ===== STATS CARDS =====
        String username = Session.getUsername();
        int total   = FileManager.getTasksForUser(username).size();
        int pending = FileManager.countPending(username);
        int done    = FileManager.countDone(username);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(new Color(30, 30, 46));
        statsPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        statsPanel.add(makeStatCard("Total Tasks", String.valueOf(total), new Color(137, 180, 250)));
        statsPanel.add(makeStatCard("Pending", String.valueOf(pending), new Color(250, 179, 135)));
        statsPanel.add(makeStatCard("Completed", String.valueOf(done), new Color(166, 227, 161)));

        // ===== ACTION BUTTONS =====
        JPanel actionsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        actionsPanel.setBackground(new Color(30, 30, 46));
        actionsPanel.setBorder(new EmptyBorder(10, 30, 30, 30));

        JButton btnAddTask = makeActionButton("➕  Add New Task", new Color(137, 180, 250));
        JButton btnViewTasks = makeActionButton("📄  View My Tasks", new Color(203, 166, 247));

        actionsPanel.add(btnAddTask);
        actionsPanel.add(btnViewTasks);

        // ===== FOOTER =====
        JPanel footer = new JPanel();
        footer.setBackground(new Color(24, 24, 37));
        footer.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel lblFooter = new JLabel("Logged in as: @" + Session.getUsername());
        lblFooter.setForeground(new Color(88, 91, 112));
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.add(lblFooter);

        // ===== ASSEMBLE =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(30, 30, 46));
        centerPanel.add(welcomePanel, BorderLayout.NORTH);
        centerPanel.add(statsPanel, BorderLayout.CENTER);
        centerPanel.add(actionsPanel, BorderLayout.SOUTH);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);
        add(mainPanel);

        // ===== ACTIONS =====
        btnAddTask.addActionListener(e -> {
            dispose();
            new AddTaskScreen().setVisible(true);
        });

        btnViewTasks.addActionListener(e -> {
            dispose();
            new ViewTasksScreen().setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                Session.logout();
                dispose();
                new LoginScreen().setVisible(true);
            }
        });
    }

    private JPanel makeStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 5));
        card.setBackground(new Color(49, 50, 68));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblValue.setForeground(accentColor);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(166, 173, 200));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblValue);
        card.add(lblTitle);
        return card;
    }

    private JButton makeActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(0, 50));
        btn.setBackground(color);
        btn.setForeground(new Color(30, 30, 46));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }
}
