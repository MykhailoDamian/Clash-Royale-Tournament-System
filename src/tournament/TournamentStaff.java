package tournament;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class TournamentStaff {
    public static void main(String[] args) {
        CompetitorList competitorList = new CompetitorList();
        loadCompetitorsFromFile(competitorList, "RunCompetitor.csv");

        // Display reports
        System.out.println("Full Details Report:\n" + competitorList.generateFullDetailsReport());
        System.out.println("Top Competitor:\n" + competitorList.findTopCompetitor());
        System.out.println("Frequency Report:\n" + competitorList.generateFrequencyReport());

        // User interaction
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Competitor Number: ");
        int number = scanner.nextInt();
        Competitor competitor = competitorList.findCompetitor(number);
        if (competitor != null) {
            System.out.println("Competitor Found: " + competitor.getFullDetails());
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
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
    }

    private static Competitor parseCompetitor(String line) {
        String[] parts = line.split(",");
        if (parts.length < 9) {
            System.out.println("Invalid data format: " + line);
            return null;
        }
        try {
            int number = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String country = parts[4].trim();
            int[] scores = new int[parts.length - 5];
            for (int i = 5; i < parts.length; i++) {
                scores[i - 5] = Integer.parseInt(parts[i].trim());
            }
            // Determine the type of competitor (Novice/Expert) and create an instance
            // This example assumes the type is determined by some logic or data in the file
            // Replace this logic with your actual criteria for determining the type
            if (isNovice(parts)) {
                return new NoviceCompetitor(number, name, country, scores);
            } else {
                return new ExpertCompetitor(number, name, country, scores);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in line: " + line);
            return null;
        }
    }

    private static boolean isNovice(String[] parts) {
        // Implement the logic to determine if the competitor is a novice
        // Example: based on age or a specific field in the data
        // ...
        return true; // Placeholder, replace with actual logic
    }
}

