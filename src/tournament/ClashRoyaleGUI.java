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
    private JComboBox<String> playerSelectDropdown; 
    
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
        addPlayerDetailViewSection(staffPanel);

        
        
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
        
        JButton deletePlayerButton = new JButton("Delete Player");
        deletePlayerButton.setBounds(360, 300, 120, 25);
        staffPanel.add(deletePlayerButton);
        deletePlayerButton.addActionListener(e -> deletePlayer((String) playerSelectDropdown.getSelectedItem()));

        JButton detailedReportButton = new JButton("Generate Detailed Report");
        detailedReportButton.setBounds(10, 350, 300, 25);
        detailedReportButton.addActionListener(e -> displayReport("Detailed Report", generateDetailedReport()));
        staffPanel.add(detailedReportButton);

        JButton totalPointsReportButton = new JButton("Generate Report Sorted by Total Points");
        totalPointsReportButton.setBounds(10, 380, 300, 25);
        totalPointsReportButton.addActionListener(e -> displayReport("Report Sorted by Total Points", generateReportSortedByTotalPoints()));
        staffPanel.add(totalPointsReportButton);

        JButton mostWinsReportButton = new JButton("Generate Report Sorted by Most Wins");
        mostWinsReportButton.setBounds(10, 410, 300, 25);
        mostWinsReportButton.addActionListener(e -> displayReport("Report Sorted by Most Wins", generateReportSortedByMostWins()));
        staffPanel.add(mostWinsReportButton);

        JButton averageScoreReportButton = new JButton("Generate Report Sorted by Average Score");
        averageScoreReportButton.setBounds(370, 350, 300, 25);
        averageScoreReportButton.addActionListener(e -> displayReport("Report Sorted by Average Score", generateReportSortedByAverageScore()));
        staffPanel.add(averageScoreReportButton);

        JButton weightedScoreReportButton = new JButton("Generate Report Sorted By Weighted Score");
        weightedScoreReportButton.setBounds(370, 380, 300, 25);
        weightedScoreReportButton.addActionListener(e -> displayReport("Report Sorted By Weighted Score", generateReportSortedByWeightedScore()));
        staffPanel.add(weightedScoreReportButton);

        JButton amountOfMatchesReportButton = new JButton("Generate Report Sorted By Amount of Matches");
        amountOfMatchesReportButton.setBounds(370, 410, 300, 25);
        amountOfMatchesReportButton.addActionListener(e -> displayReport("Report Sorted By Amount of Matches", generateReportSortedByAmountOfMatches()));
        staffPanel.add(amountOfMatchesReportButton);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds((frame.getWidth() - 100) / 2, 550, 100, 25); // Adjust as needed
        staffPanel.add(logoutButton);
        logoutButton.addActionListener(e -> logout());

        frame.revalidate();
        frame.repaint();
    }
    
    private void deletePlayer(String playerID) {
        int response = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to delete this player permanently?",
            "Delete Player",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        

        if (response == JOptionPane.YES_OPTION) {
            if (removePlayerFromDatabase(playerID)) {
                JOptionPane.showMessageDialog(frame, "Player deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Error deleting player.");
            }
        }
    }
    
    
    private boolean removePlayerFromDatabase(String playerID) {
        List<String> lines = new ArrayList<>();
        boolean playerFound = false;
        String filePath = "RunCompetitor.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].equals(playerID)) {
                    lines.add(line);
                } else {
                    playerFound = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (playerFound) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        return false;
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
    
    
    private void addPlayerDetailViewSection(JPanel panel) {
        JLabel selectPlayerLabel = new JLabel("Select Player:");
        selectPlayerLabel.setBounds(10, 300, 100, 25);
        panel.add(selectPlayerLabel);

        playerSelectDropdown = new JComboBox<>(getPlayerIDs()); // Initialize here
        playerSelectDropdown.setBounds(120, 300, 100, 25);
        panel.add(playerSelectDropdown);

        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setBounds(230, 300, 120, 25);
        panel.add(viewDetailsButton);
        viewDetailsButton.addActionListener(e -> showSelectedPlayerDetails((String) playerSelectDropdown.getSelectedItem()));
    }


    private void showSelectedPlayerDetails(String selectedPlayerID) {
        Player selectedPlayer = fetchPlayerDetails(selectedPlayerID);
        if (selectedPlayer != null) {
            // Adding match outcomes to the player
            addMatchOutcomesToPlayer(selectedPlayer);


            // Clearing the frame and setting up a new panel for player details
            JPanel playerDetailsPanel = new JPanel();
            playerDetailsPanel.setLayout(null);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(playerDetailsPanel);

            // Display player details (reuse displayPlayerDetails method)
            displayPlayerDetails(playerDetailsPanel, selectedPlayer);

            // Adjust y-position for match table to avoid overlapping
            int yPosForMatchTable = calculatePositionForMatchTable();

            // Create and add the match details table
            addMatchDetailsTable(playerDetailsPanel, selectedPlayer, yPosForMatchTable);

            // Back button to return to staff screen
            JButton backButton = new JButton("Back");
            backButton.setBounds((frame.getWidth() - 100) / 2, yPosForMatchTable + 210, 100, 25);
            playerDetailsPanel.add(backButton);
            backButton.addActionListener(e -> showTournamentStaffScreen());

            frame.revalidate();
            frame.repaint();
        } else {
            JOptionPane.showMessageDialog(frame, "Player details not found.");
        }
    }


    

	private int calculatePositionForMatchTable() {
	    return 100; 
	}
	
	private void addMatchDetailsTable(JPanel panel, Player player, int yPos) {
	    JTable matchTable = new JTable(new DefaultTableModel(new Object[]{"MatchID", "Score", "Result"}, 0));
	    DefaultTableModel model = (DefaultTableModel) matchTable.getModel();
	    model.setRowCount(0); // Clear any existing data
	
	    // Add match data to the table
	    player.getMatchOutcomes().forEach((matchID, match) -> {
	        model.addRow(new Object[]{matchID, match.getScore(), match.getResult()});
	    });
	
	    JScrollPane scrollPane = new JScrollPane(matchTable);
	    scrollPane.setBounds(10, yPos, 760, 200); // Adjust the position based on yPos
	    panel.add(scrollPane);
	}


//SORTINGS:
	
	private List<Player> getAllPlayers() {
	    List<Player> players = new ArrayList<>();
	    String filePath = "RunCompetitor.csv";
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] values = line.split(",");
	            // Assuming the player data is structured in a specific format in CSV
	            String userID = values[0];
	            String name = values[1];
	            int age = Integer.parseInt(values[2]);
	            String country = values[3];
	            String email = values[4];

	            // Extracting and parsing the scores (assuming they start from the 6th element)
	            int[] scores = Arrays.stream(Arrays.copyOfRange(values, 5, values.length))
	                                 .mapToInt(Integer::parseInt)
	                                 .toArray();

	            Player player = new Player(userID, name, age, country, email, scores);
	            
	            // Add match outcomes for each player
	            addMatchOutcomesToPlayer(player);

	            players.add(player);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return players;
	}

	// Method to generate a detailed report
	private String generateDetailedReport() {
	    List<Player> players = getAllPlayers();
	    StringBuilder report = new StringBuilder();

	    // Adding a header for the report
	    report.append("Player ID\tName\tAge\tCountry\tEmail\tTotal Points\tTotal Matches\tWins\tAverage Points\n");

	    for (Player player : players) {
	        int totalMatches = player.getMatchOutcomes().size();
	        int wins = countWins(player);
	        int totalPoints = player.getTotalScore();
	        double averagePoints = totalMatches > 0 ? (double) totalPoints / totalMatches : 0.0;

	        report.append(player.getUserID())
	              .append("\t").append(player.getName())
	              .append("\t").append(player.getAge())
	              .append("\t").append(player.getCountry())
	              .append("\t").append(player.getEmail())
	              .append("\t").append(totalPoints)
	              .append("\t").append(totalMatches)
	              .append("\t").append(wins)
	              .append("\t").append(String.format("%.1f", averagePoints)).append("\n");
	    }
	    return report.toString();
	}

	// Method to generate report sorted by average score
	private String generateReportSortedByAverageScore() {
	    List<Player> players = getAllPlayers();
	    players.sort((p1, p2) -> Double.compare(p2.getOverallScore(), p1.getOverallScore()));
	    StringBuilder report = new StringBuilder();

	    // Header
	    report.append("Player ID\tName\tAverage Score\n");

	    // Data rows
	    for (Player player : players) {
	        double averageScore = player.getOverallScore();
	        report.append(player.getUserID()).append("\t")
	              .append(player.getName()).append("\t")
	              .append(String.format("%.1f", averageScore)).append("\n");
	    }
	    return report.toString();
	}

	// Method to generate report sorted by total points
	private String generateReportSortedByTotalPoints() {
	    List<Player> players = getAllPlayers();
	    // Sort players by their total score
	    players.sort((p1, p2) -> Integer.compare(p2.getTotalScore(), p1.getTotalScore()));

	    StringBuilder report = new StringBuilder();
	    report.append("Player ID\tName\tTotal Points\n"); // Header for the report
	    for (Player player : players) {
	        // Appending only the ID, name, and total score of each player
	        report.append(player.getUserID())
	              .append("\t")
	              .append(player.getName())
	              .append("\t")
	              .append(player.getTotalScore())
	              .append("\n");
	    }
	    return report.toString();
	}


	// Method to generate report sorted by most wins
	private String generateReportSortedByMostWins() {
	    List<Player> players = getAllPlayers();
	    players.sort((p1, p2) -> Integer.compare(countWins(p2), countWins(p1)));
	    StringBuilder report = new StringBuilder();

	    // Adding a header for the report
	    report.append("Player ID\tName\tTotal Matches\tWins\n");

	    for (Player player : players) {
	        int totalMatches = player.getMatchOutcomes().size();
	        int wins = countWins(player);

	        report.append(player.getUserID())
	              .append("\t").append(player.getName())
	              .append("\t").append(totalMatches)
	              .append("\t").append(wins).append("\n");
	    }
	    return report.toString();
	}

	private int countWins(Player player) {
	    int wins = 0;
	    for (Match match : player.getMatchOutcomes().values()) {
	        if ("Win".equals(match.getResult())) {
	            wins++;
	        }
	    }
	    return wins;
	}


	private String generateReportSortedByWeightedScore() {
	    List<Player> players = getAllPlayers();
	    players.sort((p1, p2) -> Integer.compare(p2.getTotalScoreWithWeighting(), p1.getTotalScoreWithWeighting()));
	    StringBuilder report = new StringBuilder();

	    // Header
	    report.append("Player ID\tName\tWeighted Score\n");

	    // Data rows
	    for (Player player : players) {
	        int weightedScore = player.getTotalScoreWithWeighting();
	        report.append(player.getUserID()).append("\t")
	              .append(player.getName()).append("\t")
	              .append(weightedScore).append("\n");
	    }
	    return report.toString();
	}

	private String generateReportSortedByAmountOfMatches() {
	    List<Player> players = getAllPlayers();
	    players.sort((p1, p2) -> Integer.compare(p2.getMatchOutcomes().size(), p1.getMatchOutcomes().size()));
	    StringBuilder report = new StringBuilder();

	    // Header
	    report.append("Player ID\tName\tAmount of Matches\n");

	    // Data rows
	    for (Player player : players) {
	        int numberOfMatches = player.getMatchOutcomes().size();
	        report.append(player.getUserID()).append("\t")
	              .append(player.getName()).append("\t")
	              .append(numberOfMatches).append("\n");
	    }
	    return report.toString();
	}
	private void displayReport(String reportTitle, String reportContent) {
	    // Increase the size of JTextArea for a larger display
	    JTextArea textArea = new JTextArea(30, 50); // Increased rows and columns
	    textArea.setText(reportContent);
	    textArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(textArea);

	    JFrame reportFrame = new JFrame(reportTitle);
	    reportFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

	    // size for the JFrame
	    reportFrame.setPreferredSize(new Dimension(1000, 600)); 
	    reportFrame.pack();
	    reportFrame.setLocationRelativeTo(frame);
	    reportFrame.setVisible(true);
	}


	
	
	
	
	
    public static void main(String[] args) {
        new ClashRoyaleGUI();
    }
}
