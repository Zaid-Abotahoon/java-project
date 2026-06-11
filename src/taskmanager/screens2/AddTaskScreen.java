package taskmanager.screens2;

import taskmanager.FileManager;
import taskmanager.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddTaskScreen extends JFrame {

    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JComboBox<String> cmbStatus;
    private JButton btnSave;
    private JButton btnBack;
    private JLabel lblMessage;

    public AddTaskScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Task Manager - Add New Task");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 46));

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(24, 24, 37));
        topBar.setBorder(new EmptyBorder(14, 25, 14, 25));

        JButton btnBackArrow = new JButton("← Back");
        btnBackArrow.setBackground(new Color(24, 24, 37));
        btnBackArrow.setForeground(new Color(137, 180, 250));
        btnBackArrow.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBackArrow.setBorderPainted(false);
        btnBackArrow.setFocusPainted(false);
        btnBackArrow.setContentAreaFilled(false);
        btnBackArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel("➕ Add New Task");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(137, 180, 250));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        topBar.add(btnBackArrow, BorderLayout.WEST);
        topBar.add(lblTitle, BorderLayout.CENTER);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 46));
        formPanel.setBorder(new EmptyBorder(20, 35, 20, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(6, 0, 6, 0);

        // Task Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(makeLabel("Task Title"), gbc);

        gbc.gridy = 1;
        txtTitle = new JTextField();
        styleTextField(txtTitle);
        formPanel.add(txtTitle, gbc);

        // Description
        gbc.gridy = 2;
        formPanel.add(makeLabel("Description"), gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtDescription = new JTextArea(4, 20);
        txtDescription.setBackground(new Color(49, 50, 68));
        txtDescription.setForeground(new Color(205, 214, 244));
        txtDescription.setCaretColor(new Color(205, 214, 244));
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setBorder(BorderFactory.createLineBorder(new Color(88, 91, 112), 1));
        scrollDesc.getViewport().setBackground(new Color(49, 50, 68));
        formPanel.add(scrollDesc, gbc);

        // Status
        gbc.gridy = 4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(makeLabel("Status"), gbc);

        gbc.gridy = 5;
        String[] statuses = {"PENDING", "DONE"};
        cmbStatus = new JComboBox<>(statuses);
        cmbStatus.setBackground(new Color(49, 50, 68));
        cmbStatus.setForeground(new Color(205, 214, 244));
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbStatus.setBorder(BorderFactory.createLineBorder(new Color(88, 91, 112), 1));
        cmbStatus.setPreferredSize(new Dimension(0, 36));
        formPanel.add(cmbStatus, gbc);

        // Message
        gbc.gridy = 6;
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMessage.setForeground(new Color(243, 139, 168));
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblMessage, gbc);

        // Save Button
        gbc.gridy = 7;
        btnSave = new JButton("Save Task");
        stylePrimaryButton(btnSave, new Color(137, 180, 250));
        formPanel.add(btnSave, gbc);

        // ===== ASSEMBLE =====
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // ===== ACTIONS =====
        btnSave.addActionListener(e -> handleSave());
        btnBackArrow.addActionListener(e -> {
            dispose();
            new DashboardScreen().setVisible(true);
        });
    }

    private void handleSave() {
        String title       = txtTitle.getText().trim();
        String description = txtDescription.getText().trim().replace("\n", " ").replace("|", "-");
        String status      = (String) cmbStatus.getSelectedItem();

        if (title.isEmpty()) {
            lblMessage.setForeground(new Color(243, 139, 168));
            lblMessage.setText("⚠ Task title cannot be empty.");
            return;
        }
        if (title.contains("|")) {
            lblMessage.setForeground(new Color(243, 139, 168));
            lblMessage.setText("⚠ Title cannot contain '|' character.");
            return;
        }
        if (description.isEmpty()) {
            description = "No description provided";
        }

        boolean saved = FileManager.addTask(Session.getUsername(), title, description, status);
        if (saved) {
            lblMessage.setForeground(new Color(166, 227, 161));
            lblMessage.setText("✔ Task added successfully!");
            txtTitle.setText("");
            txtDescription.setText("");
            cmbStatus.setSelectedIndex(0);

            Timer timer = new Timer(900, evt -> {
                dispose();
                new DashboardScreen().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            lblMessage.setForeground(new Color(243, 139, 168));
            lblMessage.setText("✘ Failed to save task.");
        }
    }

    // ===== STYLE HELPERS =====
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(166, 173, 200));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
    }
}
