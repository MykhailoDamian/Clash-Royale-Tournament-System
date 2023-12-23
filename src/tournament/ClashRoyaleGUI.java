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
import java.util.stream.Collectors;

public class ClashRoyaleGUI {
    private JFrame frame;
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String loggedInUserID; // To store the logged-in user's ID
    private JTable scoreTable;
    private JScrollPane scrollPane; // To make the table scrollable

    public ClashRoyaleGUI() {
        frame = new JFrame("Clash Royale Tournament System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        showLoginPanel();
        frame.setVisible(true);
    }

    private void logout() {
        loggedInUserID = null;
        showLoginPanel();
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(loginPanel);

        ImageIcon titleIcon = new ImageIcon("Title.png");
        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds((frame.getWidth() - 351) / 2, 10, 351, 206);
        loginPanel.add(imageLabel);

        JLabel descriptionLabel = new JLabel("To enter an appropriate System, please enter your correct Player or Staff Credentials Below.");
        descriptionLabel.setBounds(50, 226, frame.getWidth() - 100, 50);
        descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
        loginPanel.add(descriptionLabel);

        frame.setVisible(true);
        placeComponents(loginPanel, 286);

        frame.revalidate();
        frame.repaint();
    }

    private void placeComponents(JPanel panel, int startY) {
        int startX = frame.getWidth() / 2 - 80;

        JLabel userIDLabel = new JLabel("User ID:");
        userIDLabel.setBounds(startX, startY, 80, 25);
        panel.add(userIDLabel);

        userIDField = new JTextField(20);
        userIDField.setBounds(startX + 90, startY, 160, 25);
        panel.add(userIDField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(startX, startY + 35, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(startX + 90, startY + 35, 160, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(startX + 90, startY + 70, 80, 25);
        panel.add(loginButton);
        loginButton.addActionListener(this::performLogin);
    }

    private void performLogin(ActionEvent e) {
        String userID = userIDField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Authentication auth = new Authentication(userID, password);
        if (auth.login()) {
            loggedInUserID = userID;
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
        playerPanel.setLayout(null);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(playerPanel);
        addMatchOutcomesToPlayer(player);

        ImageIcon profilePicIcon = new ImageIcon("PlayerPicture.png");
        JLabel profilePicLabel = new JLabel(profilePicIcon);
        profilePicLabel.setBounds((frame.getWidth() - 60) / 2, 10, 60, 60);
        playerPanel.add(profilePicLabel);

        JLabel welcomeLabel = new JLabel("Welcome, " + player.getName() + "!");
        welcomeLabel.setBounds((frame.getWidth() - 200) / 2, 80, 200, 20);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        playerPanel.add(welcomeLabel);

        scoreTable = new JTable(new DefaultTableModel(new Object[]{"MatchID", "Score", "Result"}, 0)) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    String result = (String) getModel().getValueAt(row, 2);
                    if ("Win".equals(result)) {
                        c.setBackground(new Color(0xCD, 0xFC, 0xC6));
                    } else if ("Loss".equals(result)) {
                        c.setBackground(new Color(0xFF, 0xD9, 0xD9));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        };

        prepareTableRenderer();
        updateScoreTable(loggedInUserID);

        scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBounds(10, 110, 760, 200);
        playerPanel.add(scrollPane);

        displayPlayerDetails(playerPanel, player);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds((frame.getWidth() - 100) / 2, 550, 100, 25);
        playerPanel.add(logoutButton);
        logoutButton.addActionListener(e -> logout());

        frame.revalidate();
        frame.repaint();
    }

    private void displayPlayerDetails(JPanel playerPanel, Player player) {
        int yPos = 320;

        String scoresString = Arrays.stream(player.getScores())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
        
        String[] details = {
                "Player ID: " + player.getUserID(),
                "Name: " + player.getName(),
                "Age: " + player.getAge(),
                "Country: " + player.getCountry(),
                "Email: " + player.getEmail(),
                "Scores: " + scoresString,
                "Average Score: " + String.format("%.1f", player.getOverallScore())
        };

        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setBounds(10, yPos, 300, 20);
            playerPanel.add(detailLabel);
            yPos += 25;
        }
        System.out.println("Average Score for " + player.getName() + ": " + player.getOverallScore());

    }

    private void updateScoreTable(String userID) {
        DefaultTableModel model = (DefaultTableModel) scoreTable.getModel();
        model.setRowCount(0);

        List<String[]> matches = readMatchesForUser(userID);
        for (String[] match : matches) {
            model.addRow(match);
        }
    }

    private List<String[]> readMatchesForUser(String userID) {
        List<String[]> matches = new ArrayList<>();
        String line;
        String csvFile = "Match.csv";

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
                    c.setBackground(new Color(0xCD, 0xFC, 0xC6));
                } else if ("Loss".equals(result)) {
                    c.setBackground(new Color(0xFF, 0xD9, 0xD9));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });
    }

    private void addMatchOutcomesToPlayer(Player player) {
        String csvFile = "Match.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[1].equals(player.getUserID())) {
                    int score = Integer.parseInt(values[2]);
                    Match match = new Match(values[0], player.getUserID(), score, values[3]);
                    player.addMatchOutcome(values[0], match);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Player fetchPlayerDetails(String userID) {
        String filePath = "RunCompetitor.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(userID)) {
                    String name = values[1];
                    int age = Integer.parseInt(values[2]);
                    String country = values[3];
                    String email = values[4];
                    
                    // Extracting and parsing the scores correctly
                    // Assuming that the scores are the elements from index 5 to the end
                    int[] scores = Arrays.stream(Arrays.copyOfRange(values, 5, values.length))
                                         .mapToInt(Integer::parseInt)
                                         .toArray();

                    // Debug statements
                    System.out.println("Full line for userID " + userID + ": " + line);
                    System.out.println("Raw score string for userID " + userID + ": " + Arrays.toString(Arrays.copyOfRange(values, 5, values.length)));
                    System.out.println("Scores for userID " + userID + ": " + Arrays.toString(scores));

                    return new Player(userID, name, age, country, email, scores);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void showTournamentStaffScreen() {
        // Implement staff screen layout and functionality
        // ...
    }

    public static void main(String[] args) {
        new ClashRoyaleGUI();
    }
}
