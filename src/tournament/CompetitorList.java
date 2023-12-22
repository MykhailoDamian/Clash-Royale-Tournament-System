package tournament;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class CompetitorList {
    private List<Competitor> competitors;

    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }

    public Competitor findCompetitor(int competitorNumber) {
        for (Competitor competitor : competitors) {
            if (competitor.getCompetitorNumber() == competitorNumber) {
                return competitor;
            }
        }
        return null;
    }

    // Method to generate a report with full details of all competitors
    public String generateFullDetailsReport() {
        StringBuilder report = new StringBuilder();
        for (Competitor competitor : competitors) {
            report.append(competitor.getFullDetails()).append("\n");
        }
        return report.toString();
    }

    // Method to find the competitor with the highest overall score
    public String findTopCompetitor() {
        if (competitors.isEmpty()) {
            return "No competitors available.";
        }

        Competitor topCompetitor = Collections.max(competitors, (c1, c2) -> Double.compare(c1.getOverallScore(), c2.getOverallScore()));
        return topCompetitor.getFullDetails();
    }

    // Method to generate a frequency report of scores
    public String generateFrequencyReport() {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (Competitor competitor : competitors) {
            for (int score : competitor.getScoreArray()) {
                frequencyMap.put(score, frequencyMap.getOrDefault(score, 0) + 1);
            }
        }

        StringBuilder report = new StringBuilder("Score Frequency:\n");
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            report.append("Score ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" times\n");
        }
        return report.toString();
    }

    // Additional methods for other statistics (average, min, max, etc.)
    // ...
}
