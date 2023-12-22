package tournament;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClashRoyaleGUI {
    private JFrame frame;
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public ClashRoyaleGUI() {
        // Initialize the main application frame
        frame = new JFrame("Clash Royale Tournament System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Show login panel initially
        showLoginPanel();
        frame.setVisible(true);
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(loginPanel);
        placeComponents(loginPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // User ID label and text field
        JLabel userIDLabel = new JLabel("User ID:");
        userIDLabel.setBounds(10, 20, 80, 25);
        panel.add(userIDLabel);

        userIDField = new JTextField(20);
        userIDField.setBounds(100, 20, 160, 25);
        panel.add(userIDField);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 160, 25);
        panel.add(passwordField);

        // Initialize and setup loginButton
        loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String userID = userIDField.getText();
        String password = new String(passwordField.getPassword());

        Authentication auth = new Authentication(userID, password);
        if (auth.login()) {
            if (auth.verifyCredentials().equals("Player")) {
                showPlayerScreen();
            } else {
                showTournamentStaffScreen();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials");
        }
    }

    private void showPlayerScreen() {
        JPanel playerPanel = new JPanel();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(playerPanel);
        playerPanel.setLayout(null);

        JLabel label = new JLabel("Player Interface");
        label.setBounds(10, 10, 160, 25);
        playerPanel.add(label);

        addLogoutButton(playerPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showTournamentStaffScreen() {
        JPanel staffPanel = new JPanel();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(staffPanel);
        staffPanel.setLayout(null);

        JLabel label = new JLabel("Tournament Staff Interface");
        label.setBounds(10, 10, 200, 25);
        staffPanel.add(label);

        addLogoutButton(staffPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void addLogoutButton(JPanel panel) {
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(10, 50, 80, 25);
        panel.add(logoutButton);

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Authentication.logout();
                showLoginPanel();
            }
        });
    }

    public static void main(String[] args) {
        new ClashRoyaleGUI();
    }
}
