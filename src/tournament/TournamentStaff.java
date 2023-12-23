package tournament;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class TournamentStaff {
    public static void main(String[] args) {
        PlayerList playerList = new PlayerList();
        loadPlayersFromFile(playerList, "RunCompetitor.csv");
        loadMatchesFromFile(playerList, "Matches.csv");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Show All Players");
            System.out.println("2. Show Top Player");
            System.out.println("3. Search Player by ID");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\nFull Details Report:\n" + playerList.generateFullDetailsReport());
                    break;
                case 2:
                    Player topPlayer = playerList.findTopPlayer();
                    if (topPlayer != null) {
                        System.out.println("\nTop Player:\n" + topPlayer.getFullDetails());
                    } else {
                        System.out.println("No players available.");
                    }
                    break;
                case 3:
                    System.out.print("\nEnter Player ID: ");
                    scanner.nextLine(); // Consume the newline
                    String userID = scanner.nextLine();
                    Player player = playerList.findPlayer(userID);
                    if (player != null) {
                        System.out.println("\nPlayer Found:\n" + player.getFullDetails());
                    } else {
                        System.out.println("Player not found.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loadPlayersFromFile(PlayerList playerList, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    System.out.println("Invalid data format: " + line);
                    continue;
                }
                String userID = parts[0].trim();
                String name = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                String country = parts[3].trim();
                String email = parts[4].trim();
                int[] scores = Arrays.stream(parts[5].split(" "))
                                     .mapToInt(Integer::parseInt)
                                     .toArray();
                playerList.addPlayer(new Player(userID, name, age, country, email, scores));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
    }

    private static void loadMatchesFromFile(PlayerList playerList, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.out.println("Invalid match data format: " + line);
                    continue;
                }
                String matchID = parts[0].trim();
                String userID = parts[1].trim();
                int score = Integer.parseInt(parts[2].trim());
                String result = parts[3].trim();

                Match match = new Match(matchID, userID, score, result);

                Player player = playerList.findPlayer(userID);
                if (player != null) {
                    player.addMatchOutcome(matchID, match);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
    }
}
