package tournament;

public class Match {
    private String matchID;
    private String userID;
    private int score;
    private String result; // "Win" or "Loss"

    public Match(String matchID, String userID, int score, String result) {
        this.matchID = matchID;
        this.userID = userID;
        this.score = score;
        this.result = result;
    }

    // Getters
    public String getMatchID() {
        return matchID;
    }

    public String getUserID() {
        return userID;
    }

    public int getScore() {
        return score;
    }

    public String getResult() {
        return result;
    }

    // Setters, if necessary
}
