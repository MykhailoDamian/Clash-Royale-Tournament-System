package tournament;

public abstract class Competitor {
    protected int competitorNumber;
    protected String name;
    protected String country;
    protected int[] scores;

    // Constructor
    public Competitor(int competitorNumber, String name, String country, int[] scores) {
        this.competitorNumber = competitorNumber;
        this.name = name;
        this.country = country;
        this.scores = scores;
    }

    // Abstract method to be implemented by subclasses
    public abstract double getOverallScore();

    // Getters
    public int getCompetitorNumber() {
        return competitorNumber;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int[] getScoreArray() {
        return scores;
    }

    // Setters
    public void setCompetitorNumber(int competitorNumber) {
        this.competitorNumber = competitorNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    // Common method to get full details of the competitor
    public String getFullDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Competitor number ").append(competitorNumber).append(", name ").append(name)
          .append(", country ").append(country).append(".\n");
        sb.append(name).append(" received these scores: ");
        for (int score : scores) {
            sb.append(score).append(" ");
        }
        sb.append("\nThis gives ").append(name).append(" an overall score of ")
          .append(String.format("%.1f", getOverallScore())).append(".");
        return sb.toString();
    }

    // Additional common methods as required
}
