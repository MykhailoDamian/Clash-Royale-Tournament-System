package tournament;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Authentication {
    private String userID;
    private String password;
    private static String currentUserID = null; // Track the logged-in user ID

    public Authentication(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String verifyCredentials() {
        String filePath = "UserCredentials.csv"; // Update with actual path
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(userID) && values[1].equals(password)) {
                    return values[2]; // Return the role if credentials match
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown"; // Return "Unknown" if credentials do not match
    }

    public boolean login() {
        String role = verifyCredentials();
        if (role.equals("Unknown")) {
            return false; // Login failed
        }
        currentUserID = userID; // Set the current user ID
        return true; // Login successful
    }

    public static void logout() {
        currentUserID = null; // Clear the current user ID
        // Logic to navigate back to the login screen
    }

    public static boolean isUserLoggedIn() {
        return currentUserID != null;
    }

    public static String getCurrentUserID() {
        return currentUserID;
    }
}
