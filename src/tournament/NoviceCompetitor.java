package tournament;

public class NoviceCompetitor extends Competitor {
    // Constructor
    public NoviceCompetitor(int competitorNumber, String name, String country, int[] scores) {
        super(competitorNumber, name, country, scores);
    }

    // Specific implementation of getOverallScore for NoviceCompetitor
    @Override
    public double getOverallScore() {
        // Assuming the overall score for a novice is the simple average of scores
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return scores.length > 0 ? (double) sum / scores.length : 0;
    }

    // You can add more methods specific to NoviceCompetitor if needed
}
