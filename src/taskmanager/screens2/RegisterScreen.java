package taskmanager.screens2;

import taskmanager.FileManager;
import taskmanager.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterScreen extends JFrame {

    private JTextField txtFullName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnBackToLogin;
    private JLabel lblMessage;

    public RegisterScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Task Manager - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 46));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(new Color(30, 30, 46));
        headerPanel.setBorder(new EmptyBorder(25, 0, 5, 0));

        JLabel lblTitle = new JLabel("📋 Create Account");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(166, 227, 161));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSub = new JLabel("Join Task Manager today");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(166, 173, 200));
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 46));
        formPanel.setBorder(new EmptyBorder(10, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(makeLabel("Full Name"), gbc);
        gbc.gridy = 1;
        txtFullName = makeTextField();
        formPanel.add(txtFullName, gbc);

        // Username
        gbc.gridy = 2;
        formPanel.add(makeLabel("Username"), gbc);
        gbc.gridy = 3;
        txtUsername = makeTextField();
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridy = 4;
        formPanel.add(makeLabel("Password"), gbc);
        gbc.gridy = 5;
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        formPanel.add(txtPassword, gbc);

        // Confirm Password
        gbc.gridy = 6;
        formPanel.add(makeLabel("Confirm Password"), gbc);
        gbc.gridy = 7;
        txtConfirmPassword = new JPasswordField();
        styleTextField(txtConfirmPassword);
        formPanel.add(txtConfirmPassword, gbc);

        // Message
        gbc.gridy = 8;
        lblMessage = new JLabel(" ");
        lblMessage.setForeground(new Color(243, 139, 168));
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblMessage, gbc);

        // Register Button
        gbc.gridy = 9;
        btnRegister = new JButton("Create Account");
        stylePrimaryButton(btnRegister, new Color(166, 227, 161));
        formPanel.add(btnRegister, gbc);

        // Back to Login
        gbc.gridy = 10;
        btnBackToLogin = new JButton("Already have an account? Login");
        styleTextButton(btnBackToLogin);
        formPanel.add(btnBackToLogin, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // ===== ACTIONS =====
        btnRegister.addActionListener(e -> handleRegister());
        btnBackToLogin.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
    }

    private void handleRegister() {
        String fullName  = txtFullName.getText().trim();
        String username  = txtUsername.getText().trim();
        String password  = new String(txtPassword.getPassword()).trim();
        String confirm   = new String(txtConfirmPassword.getPassword()).trim();

        // Validation
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("⚠ Please fill in all fields.");
            return;
        }
        if (username.contains("|") || fullName.contains("|") || password.contains("|")) {
            showError("⚠ Fields cannot contain the '|' character.");
            return;
        }
        if (username.length() < 3) {
            showError("⚠ Username must be at least 3 characters.");
            return;
        }
        if (password.length() < 4) {
            showError("⚠ Password must be at least 4 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("⚠ Passwords do not match.");
            txtPassword.setText("");
            txtConfirmPassword.setText("");
            return;
        }
        if (FileManager.userExists(username)) {
            showError("⚠ Username already taken.");
            return;
        }

        // Save
        boolean success = FileManager.registerUser(new User(username, password, fullName));
        if (success) {
            lblMessage.setForeground(new Color(166, 227, 161));
            lblMessage.setText("✔ Account created! Redirecting to login...");
            Timer timer = new Timer(1000, evt -> {
                dispose();
                new LoginScreen().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showError("✘ Failed to create account. Try again.");
        }
    }

    private void showError(String msg) {
        lblMessage.setForeground(new Color(243, 139, 168));
        lblMessage.setText(msg);
    }

    // ===== STYLE HELPERS =====
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(166, 173, 200));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private JTextField makeTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(0, 36));
        field.setBackground(new Color(49, 50, 68));
        field.setForeground(new Color(205, 214, 244));
        field.setCaretColor(new Color(205, 214, 244));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(88, 91, 112), 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void stylePrimaryButton(JButton btn, Color color) {
        btn.setPreferredSize(new Dimension(0, 40));
        btn.setBackground(color);
        btn.setForeground(new Color(30, 30, 46));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
    }

    private void styleTextButton(JButton btn) {
        btn.setBackground(new Color(30, 30, 46));
        btn.setForeground(new Color(137, 180, 250));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
