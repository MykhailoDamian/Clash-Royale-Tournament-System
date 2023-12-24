package tournament;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String userID;
    private String name;
    private int age;
    private String country;
    private String email;
    private int[] scores; // Scores for each match
    private Map<String, Match> matchOutcomes; // Map of MatchID to Match object

    public Player(String userID, String name, int age, String country, String email, int[] scores) {
        this.userID = userID;
        this.name = name;
        this.age = age;
        this.country = country;
        this.email = email;
        this.scores = scores;
        this.matchOutcomes = new HashMap<>();
    }
    public Map<String, Match> getMatchOutcomes() {
        return matchOutcomes;
    }
    public int getTotalScore() {
        return Arrays.stream(scores).sum();
    }
    public int getTotalScoreWithWeighting() {
        int total = 0;
        for (int i = 0; i < scores.length; i++) {
            String matchID = String.format("%s_%02d", userID, i + 1);
            Match match = matchOutcomes.get(matchID);

            if (match != null) {
                if (scores[i] == 1 || scores[i] == 2) {
                    total += "Win".equals(match.getResult()) ? 3 : scores[i];
                } else {
                    total += scores[i];
                }
                // Debugging statement
                System.out.println("Match: " + matchID + ", Score: " + scores[i] + ", Weighted Score: " + total);
            }
        }
        return total;
        
    }
    // Add a match outcome to the player
    public void addMatchOutcome(String matchID, Match match) {
        this.matchOutcomes.put(matchID, match);
     // Debugging statement
        System.out.println("Added match outcome: " + matchID + " - Result: " + match.getResult() + ", Score: " + match.getScore());
    }

    // Calculate the overall score based on match outcomes and scores
    public double getOverallScore() {
        double total = 0;
        for (int i = 0; i < scores.length; i++) {
            String matchID = String.format("%s_%02d", userID, i + 1);
            Match match = matchOutcomes.get(matchID);

            if (match != null) {
                // Apply the new weighing logic
                if (scores[i] == 1 || scores[i] == 2) {
                    total += "Win".equals(match.getResult()) ? 3 : scores[i];
                } else {
                    total += scores[i];
                }
                System.out.println("Overall Score for " + userID + ": " + total);
            }
        }
        return scores.length > 0 ? total / scores.length : 0; // Average score
    }

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public int[] getScores() {
        return scores;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    // Method to return full details of the player
    public String getFullDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ID: ").append(userID)
          .append(", Name: ").append(name)
          .append(", Age: ").append(age)
          .append(", Country: ").append(country)
          .append(", Email: ").append(email)
          .append(", Scores: ");
        for (int score : scores) {
            sb.append(score).append(" ");
        }
        sb.append(", Overall Score: ").append(String.format("%.1f", getOverallScore()));
        return sb.toString();
    }
}
