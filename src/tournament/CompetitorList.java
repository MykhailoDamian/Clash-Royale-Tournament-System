package tournament;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CompetitorList {
    private List<Competitor> competitors;

    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    // Add a competitor to the list
    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }

    // Find a competitor by their number
    public Competitor findCompetitor(int competitorNumber) {
        for (Competitor competitor : competitors) {
            if (competitor.getCompetitorNumber() == competitorNumber) {
                return competitor;
            }
        }
        return null; // Competitor not found
    }

    // Generate a report with full details of all competitors
    public String generateFullDetailsReport() {
        StringBuilder report = new StringBuilder();
        for (Competitor competitor : competitors) {
            report.append(competitor.getFullDetails()).append("\n");
        }
        return report.toString();
    }

    // Find the competitor with the highest overall score
    public Competitor findTopCompetitor() {
        if (competitors.isEmpty()) {
            return null; // No competitors available
        }

        Competitor topCompetitor = competitors.get(0);
        for (Competitor competitor : competitors) {
            if (competitor.getOverallScore() > topCompetitor.getOverallScore()) {
                topCompetitor = competitor;
            }
        }
        return topCompetitor;
    }

    // Generate a frequency report of scores
    public String generateFrequencyReport() {
        Map<Integer, Integer> scoreFrequency = new HashMap<>();

        // Tally each score for all competitors
        for (Competitor competitor : competitors) {
            for (int score : competitor.getScoreArray()) {
                scoreFrequency.put(score, scoreFrequency.getOrDefault(score, 0) + 1);
            }
        }

        // Building the report string
        StringBuilder report = new StringBuilder("Score Frequency Report:\n");
        scoreFrequency.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> report.append("Score ")
                                    .append(entry.getKey())
                                    .append(": ")
                                    .append(entry.getValue())
                                    .append(" times\n"));
        
        return report.toString();
    }

    // Additional methods for other statistics (average, min, max, etc.)
    // ...
}
