package taskmanager.screens2;

import taskmanager.FileManager;
import taskmanager.Session;
import taskmanager.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblMessage;

    public LoginScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Task Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 30, 46));
        mainPanel.setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 30, 46));
        headerPanel.setBorder(new EmptyBorder(30, 0, 10, 0));
        JLabel lblTitle = new JLabel("📋 Task Manager");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(203, 166, 247));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblSub = new JLabel("Sign in to your account");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(166, 173, 200));
        headerPanel.setLayout(new GridLayout(2, 1, 0, 5));
        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(30, 30, 46));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        // Username label
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUser = new JLabel("Username");
        lblUser.setForeground(new Color(166, 173, 200));
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(lblUser, gbc);

        // Username field
        gbc.gridy = 1;
        txtUsername = createTextField("Enter your username");
        formPanel.add(txtUsername, gbc);

        // Password label
        gbc.gridy = 2;
        JLabel lblPass = new JLabel("Password");
        lblPass.setForeground(new Color(166, 173, 200));
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(lblPass, gbc);

        // Password field
        gbc.gridy = 3;
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        txtPassword.setToolTipText("Enter your password");
        formPanel.add(txtPassword, gbc);

        // Message label
        gbc.gridy = 4;
        lblMessage = new JLabel(" ");
        lblMessage.setForeground(new Color(243, 139, 168));
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblMessage, gbc);

        // Login Button
        gbc.gridy = 5;
        btnLogin = new JButton("Login");
        stylePrimaryButton(btnLogin, new Color(137, 180, 250));
        formPanel.add(btnLogin, gbc);

        // Register Button
        gbc.gridy = 6;
        btnRegister = new JButton("Don't have an account? Register");
        styleTextButton(btnRegister);
        formPanel.add(btnRegister, gbc);

        // ===== ASSEMBLE =====
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // ===== ACTIONS =====
        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterScreen().setVisible(true);
        });

        // Allow Enter key to login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("⚠ Please fill in all fields.");
            return;
        }

        User user = FileManager.validateLogin(username, password);
        if (user != null) {
            Session.login(user);
            lblMessage.setForeground(new Color(166, 227, 161));
            lblMessage.setText("✔ Welcome back, " + user.getFullName() + "!");
            Timer timer = new Timer(800, evt -> {
                dispose();
                new DashboardScreen().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            lblMessage.setForeground(new Color(243, 139, 168));
            lblMessage.setText("✘ Invalid username or password.");
            txtPassword.setText("");
        }
    }

    // ===== STYLE HELPERS =====
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(0, 38));
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
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
