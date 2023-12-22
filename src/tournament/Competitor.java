package tournament;

public class Competitor {
    private int competitorNumber;
    private String name;
    private String country;
    private int age;
    private String level;
    private int[] scores;

    // Constructor
    public Competitor(int competitorNumber, String name, String country, int age, String level, int[] scores) {
        this.competitorNumber = competitorNumber;
        this.name = name;
        this.country = country;
        this.age = age;
        this.level = level;
        this.scores = scores;
    }

    // Getters and setters for all attributes
    // ...

    // Method to get the array of scores
    public int[] getScoreArray() {
        return scores;
    }

    // Method to calculate and get overall score
    public double getOverallScore() {
        double weightedSum = 0;
        int weightSum = 0;
        for (int i = 0; i < scores.length; i++) {
            int weight = getWeightForLevel(i);
            weightedSum += scores[i] * weight;
            weightSum += weight;
        }
        return weightedSum / weightSum;
    }

    // Method to determine weight based on level
    private int getWeightForLevel(int scoreIndex) {
        // Example: Different weights based on the level
        if (level.equalsIgnoreCase("Novice")) {
            return 1; // Equal weight for all scores for Novice level
        } else if (level.equalsIgnoreCase("Expert")) {
            // Higher weight for later scores for Expert level
            return scoreIndex + 1;
        } else {
            return 1; // Default weight
        }
    }

    // Method to get full details including scores
    public String getFullDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Competitor number ").append(competitorNumber).append(", name ").append(name).append(", country ").append(country).append(".\n");
        details.append(name).append(" is a ").append(level).append(" aged ").append(age).append(" and received these scores: ");
        for (int score : scores) {
            details.append(score).append(" ");
        }
        details.append("\nThis gives him an overall score of ").append(String.format("%.1f", getOverallScore())).append(".");
        return details.toString();
    }

    // Method to get short details
    public String getShortDetails() {
        return "CN " + competitorNumber + " (" + getInitials(name) + ") has overall score " + String.format("%.1f", getOverallScore()) + ".";
    }

    // Helper method to get initials from name
    private String getInitials(String name) {
        String[] words = name.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            initials.append(word.charAt(0));
        }
        return initials.toString();
    }
}
