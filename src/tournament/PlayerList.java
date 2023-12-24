package tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class PlayerList {
    private List<Player> players;

    public PlayerList() {
        this.players = new ArrayList<>();
    }

    // Add a player to the list
    public void addPlayer(Player player) {
        players.add(player);
    }

    // Find a player by their ID
    public Player findPlayer(String userID) {
        for (Player player : players) {
            if (player.getUserID().equals(userID)) {
                return player;
            }
        }
        return null; // Player not found
    }

    // Generate a report with full details of all players
    public String generateFullDetailsReport() {
        StringBuilder report = new StringBuilder();
        for (Player player : players) {
            report.append(player.getFullDetails()).append("\n");
        }
        return report.toString();
    }

    // Find the player with the highest overall score
    public Player findTopPlayer() {
        return players.stream()
                      .max(Comparator.comparingDouble(Player::getOverallScore))
                      .orElse(null); // Returns null if no players are available
    }

    // Generate a frequency report of scores
    public String generateFrequencyReport() {
        // Implementation of frequency report logic
        // ...
        return "Frequency Report: Not Implemented Yet";
    }

}
