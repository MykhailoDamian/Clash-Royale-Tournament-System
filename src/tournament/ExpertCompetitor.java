package tournament;

public class ExpertCompetitor extends Competitor {
    // Constructor
    public ExpertCompetitor(int competitorNumber, String name, String country, int[] scores) {
        super(competitorNumber, name, country, scores);
    }

    // Specific implementation of getOverallScore for ExpertCompetitor
    @Override
    public double getOverallScore() {
        double weightedSum = 0;
        int weightSum = 0;
        for (int i = 0; i < scores.length; i++) {
            int weight = i + 1; // Increasing weight for each subsequent score
            weightedSum += scores[i] * weight;
            weightSum += weight;
        }
        return weightSum > 0 ? weightedSum / weightSum : 0;
    }

    // more methods specific to ExpertCompetitor if needed
}
