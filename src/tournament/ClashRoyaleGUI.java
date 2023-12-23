package tournament;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
        String totalScoreString = String.valueOf(player.getTotalScore());
        String totalScoreWithWeightingString = String.valueOf(player.getTotalScoreWithWeighting());
        String averageScoreString = String.format("%.1f", player.getOverallScore());

        String scoresString = Arrays.stream(player.getScores())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
        
        String[] details = {
                "Player ID: " + player.getUserID(),
                "Name: " + player.getName(),
                "Age: " + player.getAge(),
                "Country: " + player.getCountry(),
                "Email: " + player.getEmail(),
                "Total Score: " + totalScoreString,
                "Total Score With Weighting: " + totalScoreWithWeightingString,
                "Average Score: " + averageScoreString
        };

        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setBounds(10, yPos, 300, 20);
            if (detail.startsWith("Total Score") || detail.startsWith("Total Score With Weighting")) {
                detailLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Make text bold
            }
            if (detail.startsWith("Average Score")) {
                detailLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Highlight the average score
                detailLabel.setForeground(Color.RED); // Change color to highlight
            }
            playerPanel.add(detailLabel);
            yPos += 25;
        }

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
        JPanel staffPanel = new JPanel();
        staffPanel.setLayout(null);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(staffPanel);

        // Default profile picture
        ImageIcon profilePicIcon = new ImageIcon("StaffPicture.png"); // Replace with your image path
        JLabel profilePicLabel = new JLabel(profilePicIcon);
        profilePicLabel.setBounds((frame.getWidth() - 60) / 2, 10, 60, 60); // Adjust as needed
        staffPanel.add(profilePicLabel);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, Tournament Staff!");
        welcomeLabel.setBounds((frame.getWidth() - 200) / 2, 80, 200, 20); // Adjust as needed
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        staffPanel.add(welcomeLabel);
        
        addCreateMatchSection(staffPanel);
        
        JLabel titleLabel = new JLabel("Create Match");
        titleLabel.setBounds(10, 100, 100, 25);
        staffPanel.add(titleLabel);

        // Dropdowns for Player 1 and Player 2
        JComboBox<String> player1Dropdown = new JComboBox<>(getPlayerIDs());
        JComboBox<String> player2Dropdown = new JComboBox<>(getPlayerIDs());
        player1Dropdown.setBounds(10, 130, 100, 25);
        player2Dropdown.setBounds(120, 130, 100, 25);
        staffPanel.add(player1Dropdown);
        staffPanel.add(player2Dropdown);

        // Points input fields for Player 1 and Player 2
        JTextField points1Field = new JTextField();
        JTextField points2Field = new JTextField();
        points1Field.setBounds(230, 130, 50, 25);
        points2Field.setBounds(290, 130, 50, 25);
        staffPanel.add(points1Field);
        staffPanel.add(points2Field);

        // Dropdown for Result
        JComboBox<String> resultDropdown = new JComboBox<>(new String[] {"Player 1 Win", "Player 2 Win"});
        resultDropdown.setBounds(350, 130, 100, 25);
        staffPanel.add(resultDropdown);

        // Add Match button
        JButton addMatchButton = new JButton("Add Match");
        addMatchButton.setBounds(460, 130, 100, 25);
        staffPanel.add(addMatchButton);
        addMatchButton.addActionListener(e -> addMatch(player1Dropdown, player2Dropdown, points1Field, points2Field, resultDropdown));

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds((frame.getWidth() - 100) / 2, 550, 100, 25); // Adjust as needed
        staffPanel.add(logoutButton);
        logoutButton.addActionListener(e -> logout());

        frame.revalidate();
        frame.repaint();
    }
    private void addCreateMatchSection(JPanel panel) {
        JLabel titleLabel = new JLabel("Create Match");
        titleLabel.setBounds(10, 100, 100, 25);
        panel.add(titleLabel);

        JComboBox<String> player1Dropdown = new JComboBox<>(getPlayerIDs());
        JComboBox<String> player2Dropdown = new JComboBox<>(getPlayerIDs());
        JTextField points1Field = new JTextField();
        JTextField points2Field = new JTextField();
        JComboBox<String> resultDropdown = new JComboBox<>(new String[]{"Player 1 Win", "Player 2 Win"});

        player1Dropdown.setBounds(10, 130, 100, 25);
        player2Dropdown.setBounds(120, 130, 100, 25);
        points1Field.setBounds(230, 130, 50, 25);
        points2Field.setBounds(290, 130, 50, 25);
        resultDropdown.setBounds(350, 130, 100, 25);

        JButton addMatchButton = new JButton("Add Match");
        addMatchButton.setBounds(460, 130, 100, 25);
        addMatchButton.addActionListener(e -> addMatch(player1Dropdown, player2Dropdown, points1Field, points2Field, resultDropdown));

        panel.add(player1Dropdown);
        panel.add(player2Dropdown);
        panel.add(points1Field);
        panel.add(points2Field);
        panel.add(resultDropdown);
        panel.add(addMatchButton);
    }
    private String[] getPlayerIDs() {
        List<String> playerIDs = new ArrayList<>();
        String filePath = "RunCompetitor.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                playerIDs.add(values[0]); // Assuming the first column is the player ID
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerIDs.toArray(new String[0]);
    }


    private void addMatch(JComboBox<String> player1Dropdown, JComboBox<String> player2Dropdown, JTextField points1Field, JTextField points2Field, JComboBox<String> resultDropdown) {
        String player1ID = (String) player1Dropdown.getSelectedItem();
        String player2ID = (String) player2Dropdown.getSelectedItem();
        if (player1ID.equals(player2ID)) {
            JOptionPane.showMessageDialog(frame, "Players must be different.");
            return;
        }

        int points1, points2;
        try {
            points1 = Integer.parseInt(points1Field.getText());
            points2 = Integer.parseInt(points2Field.getText());
            if (points1 < 0 || points1 > 3 || points2 < 0 || points2 > 3 || (points1 == 3 && points2 == 3)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, "Invalid points. Please enter numbers from 0 to 3.");
            return;
        }

        String result = (String) resultDropdown.getSelectedItem();
        String winnerID = result.equals("Player 1 Win") ? player1ID : player2ID;
        updateMatchCsv(player1ID, player2ID, points1, points2, winnerID);
        updateRunCompetitorCsv(player1ID, player2ID, points1, points2);
    }

    private void updateMatchCsv(String player1ID, String player2ID, int points1, int points2, String winnerID) {
        String filePath = "Match.csv";
        try (FileWriter fw = new FileWriter(filePath, true); 
             BufferedWriter bw = new BufferedWriter(fw)) {

            String player1Result = player1ID.equals(winnerID) ? "Win" : "Loss";
            String player2Result = player2ID.equals(winnerID) ? "Win" : "Loss";

            int player1MatchNumber = getPlayerMatchNumber(player1ID) + 1;
            int player2MatchNumber = getPlayerMatchNumber(player2ID) + 1;

            String player1Record = String.format("%s_%02d,%s,%d,%s\n", player1ID, player1MatchNumber, player1ID, points1, player1Result);
            String player2Record = String.format("%s_%02d,%s,%d,%s\n", player2ID, player2MatchNumber, player2ID, points2, player2Result);

            bw.write(player1Record);
            bw.write(player2Record);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getPlayerMatchNumber(String playerID) {
        String filePath = "Match.csv";
        int matchCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(playerID)) {
                    matchCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matchCount;
    }

    private void updateRunCompetitorCsv(String player1ID, String player2ID, int points1, int points2) {
        String filePath = "RunCompetitor.csv";
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(player1ID)) {
                    line = appendNewScore(line, points1);
                } else if (line.startsWith(player2ID)) {
                    line = appendNewScore(line, points2);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String appendNewScore(String line, int newScore) {
        return line + "," + newScore;
    }


    public static void main(String[] args) {
        new ClashRoyaleGUI();
    }
}
