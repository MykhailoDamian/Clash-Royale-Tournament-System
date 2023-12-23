package tournament;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class ClashRoyaleGUI {
    private JFrame frame;
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String loggedInUserID; // To store the logged-in user's ID


    public ClashRoyaleGUI() {
        // Initialize the main application frame
        frame = new JFrame("Clash Royale Tournament System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // Increased window size

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
        loginButton.addActionListener(this::performLogin);
    }

    private void performLogin(ActionEvent e) {
        String userID = userIDField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Authentication auth = new Authentication(userID, password);
        if (auth.login()) {
            loggedInUserID = userID; // Store the logged-in user ID
            String userRole = auth.verifyCredentials();
            if ("Player".equals(userRole)) {
                Player player = fetchPlayerDetails(loggedInUserID);
                if (player != null) {
                    showPlayerScreen(player);
                } else {
                    JOptionPane.showMessageDialog(frame, "Player details not found.");
                }
            } else if ("TournamentStaff".equals(userRole)) {
                showTournamentStaffScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid user role");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials");
        }
    }

    private void showPlayerScreen(Player player) {
        JPanel playerPanel = new JPanel();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(playerPanel);
        playerPanel.setLayout(null);

        // Display player details in a text area
        JTextArea detailsArea = new JTextArea();
        detailsArea.setBounds(10, 10, 580, 340);
        detailsArea.setEditable(false);
        detailsArea.setText(player.getFullDetails());
        playerPanel.add(detailsArea);

        addLogoutButton(playerPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showTournamentStaffScreen() {
        // Implement the staff screen layout and functionality
        // ...
    }

    private void addLogoutButton(JPanel panel) {
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(10, 350, 80, 25);
        panel.add(logoutButton);
        logoutButton.addActionListener(e -> showLoginPanel());
    }

    private Player fetchPlayerDetails(String userID) {
        String filePath = "RunCompetitor.csv"; // Update with actual path
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(userID)) {
                    String name = values[1];
                    int age = Integer.parseInt(values[2]);
                    String country = values[3];
                    String email = values[4];
                    int[] scores = Arrays.stream(values[5].split(" "))
                                         .mapToInt(Integer::parseInt)
                                         .toArray();
                    return new Player(userID, name, age, country, email, scores);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new ClashRoyaleGUI();
    }
}
