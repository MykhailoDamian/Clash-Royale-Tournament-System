package tournament;

public class Main {
    public static void main(String[] args) {
        // Sample data for testing
        int[] scores = {5, 4, 5, 4, 3};
        Competitor competitor = new Competitor(100, "Keith John Talbot", "UK", 21, "Novice", scores);

        // Test methods
        System.out.println(competitor.getFullDetails());
        System.out.println(competitor.getShortDetails());
    }
}


