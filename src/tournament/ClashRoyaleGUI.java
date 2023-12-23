package tournament;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ClashRoyaleGUI {
    private JFrame frame;
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String loggedInUserID; // To store the logged-in user's ID
    private JTable scoreTable;
    private JScrollPane scrollPane; // To make the table scrollable


    public ClashRoyaleGUI() {
        // Initialize the main application frame
        frame = new JFrame("Clash Royale Tournament System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Increased window size

        // Show login panel initially
        showLoginPanel();
        frame.setVisible(true);
    }
    private void logout() {
        // Clear the session data, if any
        loggedInUserID = null;
        
        // Show the login panel
        showLoginPanel();
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null); // Continue using absolute positioning
        frame.getContentPane().removeAll();
        frame.getContentPane().add(loginPanel);

        // Load the image as an ImageIcon
        ImageIcon titleIcon = new ImageIcon("Title.png"); // Replace with actual path
        // Create a label to contain the image
        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds((frame.getWidth() - 351) / 2, 10, 351, 206); // Center the image label horizontally
        loginPanel.add(imageLabel);

        // Create the description label
        JLabel descriptionLabel = new JLabel("To enter an appropriate System, please enter your correct Player or Staff Credentials Below.");
        descriptionLabel.setBounds(50, 226, frame.getWidth() - 100, 50); // Center the text label horizontally
        descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
        loginPanel.add(descriptionLabel);

        // Add the rest of the components
        frame.setVisible(true); // The frame needs to be visible to correctly calculate sizes
        placeComponents(loginPanel, 286); // Start the login components below the description

        frame.revalidate();
        frame.repaint();
    }

    private void placeComponents(JPanel panel, int startY) {
        int startX = frame.getWidth() / 2 - 80; // Half the frame width minus half the total width of the components for centering

        // User ID label and text field
        JLabel userIDLabel = new JLabel("User ID:");
        userIDLabel.setBounds(startX, startY, 80, 25);
        panel.add(userIDLabel);

        userIDField = new JTextField(20);
        userIDField.setBounds(startX + 90, startY, 160, 25); // Position to the right of the label
        panel.add(userIDField);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(startX, startY + 35, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(startX + 90, startY + 35, 160, 25); // Position to the right of the label
        panel.add(passwordField);

        // Initialize and setup loginButton
        loginButton = new JButton("Login");
        loginButton.setBounds(startX + 90, startY + 70, 80, 25); // Position to the right of the text fields
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
    
    //Player Screen 

    private void showPlayerScreen(Player player) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(null);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(playerPanel);

        // Default profile picture centered at the top
        ImageIcon profilePicIcon = new ImageIcon("PlayerPicture.png"); // Replace with your image path
        JLabel profilePicLabel = new JLabel(profilePicIcon);
        profilePicLabel.setBounds((frame.getWidth() - 60) / 2, 10, 60, 60); // Center the profile picture
        playerPanel.add(profilePicLabel);

        // Welcome message centered below the profile picture
        JLabel welcomeLabel = new JLabel("Welcome, " + player.getName() + "!");
        welcomeLabel.setBounds((frame.getWidth() - 200) / 2, 80, 200, 20); // Adjust the size as needed
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        playerPanel.add(welcomeLabel);

        // Initialize the score table with the model and override the prepareRenderer method
        scoreTable = new JTable(new DefaultTableModel(new Object[]{"MatchID", "Score", "Result"}, 0)) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    String result = (String) getModel().getValueAt(row, 2);
                    if ("Win".equals(result)) {
                        c.setBackground(new Color(0xCD, 0xFC, 0xC6)); // Slightly green for win
                    } else if ("Loss".equals(result)) {
                        c.setBackground(new Color(0xFF, 0xD9, 0xD9)); // Slightly red for loss
                    } else {
                        c.setBackground(Color.WHITE); // Default background
                    }
                }
                return c;
            }
        };

        // Call this to set up the table's custom renderer
        prepareTableRenderer();

        // Populate the score table with data for the logged-in user
        updateScoreTable(loggedInUserID);

        // Set the table in a scroll pane and add it to the player panel
        scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBounds(10, 110, 760, 200); // Adjusted y position to prevent overlap
        playerPanel.add(scrollPane);
        // Player details displayed using labels for better formatting
        int yPos = 320; // Adjusted y position to start after the score table
        String[] details = {
            "Player ID: " + player.getUserID(),
            "Name: " + player.getName(),
            "Age: " + player.getAge(),
            "Country: " + player.getCountry(),
            "Email: " + player.getEmail(),
            "Average Score: " + String.format("%.1f", player.getOverallScore())
        };

        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setBounds(10, yPos, 300, 20); // Set bounds as needed
            playerPanel.add(detailLabel);
            yPos += 25; // Increment y position for the next label
        }

        // Logout button centered at the bottom
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds((frame.getWidth() - 100) / 2, yPos + 20, 100, 25); // Adjust yPos as needed
        playerPanel.add(logoutButton);
        logoutButton.addActionListener(e -> logout()); // Add an action listener to call the logout method

        // Refresh the frame to display the updated panel
        frame.setSize(800, 600); // Set the frame size to accommodate all components
        frame.revalidate();
        frame.repaint();
    }

    
    private void updateScoreTable(String userID) {
        DefaultTableModel model = (DefaultTableModel) scoreTable.getModel();
        model.setRowCount(0); // Clear existing data

        List<String[]> matches = readMatchesForUser(userID);
        for (String[] match : matches) {
            model.addRow(match);
        }
    }

    private List<String[]> readMatchesForUser(String userID) {
        List<String[]> matches = new ArrayList<>();
        String line;
        String csvFile = "Match.csv"; // Path to your CSV file

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[1].equals(userID)) {
                    matches.add(new String[]{values[0], values[2], values[3]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }

    private void prepareTableRenderer() {
        scoreTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String result = (String) table.getModel().getValueAt(row, 2);
                if ("Win".equals(result)) {
                    c.setBackground(new Color(0xCD, 0xFC, 0xC6)); // Slightly green for win
                } else if ("Loss".equals(result)) {
                    c.setBackground(new Color(0xFF, 0xD9, 0xD9)); // Slightly red for loss
                } else {
                    c.setBackground(Color.WHITE); // Default background
                }
                return c;
            }
        });
    }


    private void showTournamentStaffScreen() {
        // Implement the staff screen layout and functionality
        // ...
    }


    private Player fetchPlayerDetails(String userID) {
        String filePath = "RunCompetitor.csv"; // Path to your CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(userID)) {
                    String name = values[1];
                    int age = Integer.parseInt(values[2]);
                    String country = values[3];
                    String email = values[4];
                    
                    // Assuming scores are stored in the 6th column and are space-separated
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
