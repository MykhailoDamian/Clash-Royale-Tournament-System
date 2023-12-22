package tournament;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Manager {
    public static void main(String[] args) {
        CompetitorList competitorList = new CompetitorList();
        loadCompetitorsFromFile(competitorList, "RunCompetitor.csv");

        // Generate and display reports
        System.out.println("Full Details Report:\n" + competitorList.generateFullDetailsReport());
        System.out.println("Top Competitor:\n" + competitorList.findTopCompetitor());
        System.out.println("Frequency Report:\n" + competitorList.generateFrequencyReport());

        // User interaction for competitor details
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Competitor Number: ");
        int number = scanner.nextInt();
        Competitor competitor = competitorList.findCompetitor(number);
        if (competitor != null) {
            System.out.println("Competitor Found: " + competitor.getShortDetails());
        } else {
            System.out.println("Competitor not found.");
        }
    }

    private static void loadCompetitorsFromFile(CompetitorList competitorList, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Competitor competitor = parseCompetitor(line);
                if (competitor != null) {
                    competitorList.addCompetitor(competitor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Competitor parseCompetitor(String line) {
        String[] parts = line.split(",");
        // Expecting at least 9 parts: number, name, age, gender, country, and four scores
        if (parts.length < 9) {
            System.out.println("Invalid data format: " + line);
            return null;
        }
        try {
            int number = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            // Skipping age and gender as they are not used in Competitor constructor
            String country = parts[4].trim();
            int[] scores = new int[parts.length - 5];
            for (int i = 5; i < parts.length; i++) {
                scores[i - 5] = Integer.parseInt(parts[i].trim());
            }
            // Sample level, update as needed
            return new Competitor(number, name, country, 25, "Novice", scores);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in line: " + line);
            return null;
        }
    }
}
