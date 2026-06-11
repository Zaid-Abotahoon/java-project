package taskmanager.screens2;

import taskmanager.FileManager;
import taskmanager.Session;
import taskmanager.models.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ViewTasksScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblMessage;
    private List<Task> userTasks;

    // Column indices
    private static final int COL_ID     = 0;
    private static final int COL_TITLE  = 1;
    private static final int COL_DESC   = 2;
    private static final int COL_STATUS = 3;
    private static final int COL_DATE   = 4;

    public ViewTasksScreen() {
        initUI();
        loadTasks();
    }

    private void initUI() {
        setTitle("Task Manager - My Tasks");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 500);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 46));

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(24, 24, 37));
        topBar.setBorder(new EmptyBorder(14, 25, 14, 25));

        JButton btnBack = new JButton("← Dashboard");
        btnBack.setBackground(new Color(24, 24, 37));
        btnBack.setForeground(new Color(137, 180, 250));
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel("📄 My Tasks");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(203, 166, 247));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        topBar.add(btnBack, BorderLayout.WEST);
        topBar.add(lblTitle, BorderLayout.CENTER);

        // ===== TABLE =====
        String[] columns = {"ID", "Title", "Description", "Status", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };

        table = new JTable(tableModel);
        table.setBackground(new Color(49, 50, 68));
        table.setForeground(new Color(205, 214, 244));
        table.setGridColor(new Color(88, 91, 112));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(88, 91, 112));
        table.setSelectionForeground(new Color(205, 214, 244));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.getTableHeader().setBackground(new Color(24, 24, 37));
        table.getTableHeader().setForeground(new Color(166, 173, 200));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);

        // Column widths
        table.getColumnModel().getColumn(COL_ID).setPreferredWidth(35);
        table.getColumnModel().getColumn(COL_ID).setMaxWidth(45);
        table.getColumnModel().getColumn(COL_TITLE).setPreferredWidth(140);
        table.getColumnModel().getColumn(COL_DESC).setPreferredWidth(220);
        table.getColumnModel().getColumn(COL_STATUS).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_DATE).setPreferredWidth(90);

        // Status column color renderer
        table.getColumnModel().getColumn(COL_STATUS).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if ("DONE".equals(value)) {
                    setForeground(new Color(166, 227, 161));
                    setText("✔ DONE");
                } else {
                    setForeground(new Color(250, 179, 135));
                    setText("⏳ PENDING");
                }
                setBackground(isSelected ? new Color(88, 91, 112) : new Color(49, 50, 68));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(49, 50, 68));
        scrollPane.getViewport().setBackground(new Color(49, 50, 68));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // ===== BOTTOM BUTTONS =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(24, 24, 37));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMessage.setForeground(new Color(166, 173, 200));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(new Color(24, 24, 37));

        JButton btnToggle = makeButton("Toggle Status", new Color(250, 179, 135));
        JButton btnDelete = makeButton("Delete Task", new Color(243, 139, 168));
        JButton btnAdd    = makeButton("+ Add Task", new Color(166, 227, 161));

        btnPanel.add(btnToggle);
        btnPanel.add(btnDelete);
        btnPanel.add(btnAdd);

        bottomPanel.add(lblMessage, BorderLayout.WEST);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        // ===== ASSEMBLE =====
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // ===== ACTIONS =====
        btnBack.addActionListener(e -> {
            dispose();
            new DashboardScreen().setVisible(true);
        });

        btnAdd.addActionListener(e -> {
            dispose();
            new AddTaskScreen().setVisible(true);
        });

        btnToggle.addActionListener(e -> handleToggleStatus());
        btnDelete.addActionListener(e -> handleDelete());
    }

    // Load tasks from file and populate table
    private void loadTasks() {
        tableModel.setRowCount(0);
        userTasks = FileManager.getTasksForUser(Session.getUsername());
        for (Task task : userTasks) {
            tableModel.addRow(new Object[]{
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDate()
            });
        }
        if (userTasks.isEmpty()) {
            lblMessage.setText("No tasks yet. Add your first task!");
        } else {
            lblMessage.setText("Total: " + userTasks.size() + " task(s)");
        }
    }

    private void handleToggleStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showMsg("⚠ Please select a task first.", new Color(250, 179, 135));
            return;
        }

        int taskId      = (int) tableModel.getValueAt(selectedRow, COL_ID);
        String currStatus = userTasks.get(selectedRow).getStatus();
        String newStatus  = currStatus.equals("PENDING") ? "DONE" : "PENDING";

        boolean ok = FileManager.updateTaskStatus(taskId, newStatus);
        if (ok) {
            showMsg("✔ Status updated to " + newStatus, new Color(166, 227, 161));
            loadTasks();
        } else {
            showMsg("✘ Failed to update status.", new Color(243, 139, 168));
        }
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showMsg("⚠ Please select a task first.", new Color(250, 179, 135));
            return;
        }

        String taskTitle = (String) tableModel.getValueAt(selectedRow, COL_TITLE);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete task: \"" + taskTitle + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        int taskId = (int) tableModel.getValueAt(selectedRow, COL_ID);
        boolean ok = FileManager.deleteTask(taskId);
        if (ok) {
            showMsg("✔ Task deleted.", new Color(166, 227, 161));
            loadTasks();
        } else {
            showMsg("✘ Failed to delete task.", new Color(243, 139, 168));
        }
    }

    private void showMsg(String msg, Color color) {
        lblMessage.setForeground(color);
        lblMessage.setText(msg);
    }

    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(new Color(30, 30, 46));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 34));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }
}
