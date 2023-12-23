package tournament;

public class Match {
    private String matchID;
    private String userID; // The user associated with the match
    private int score;     // The score in the match
    private String result; // The result of the match (e.g., Win/Loss)

    // Constructor with matchID, userID, score, and result
    public Match(String matchID, String userID, int score, String result) {
        this.matchID = matchID;
        this.userID = userID;
        this.score = score;
        this.result = result;
    }

    // Getters and setters for each field
    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    // Additional methods or logic can be added as needed
}
