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

    // Method to get overall score
    public double getOverallScore() {
        return 5; // Static value for now
    }

    // Method to get full details
    public String getFullDetails() {
        return "Competitor number " + competitorNumber + ", name " + name + ", country " + country + ".\n" +
               name + " is a " + level + " aged " + age + " and has an overall score of " + getOverallScore() + ".";
    }

    // Method to get short details
    public String getShortDetails() {
        return "CN " + competitorNumber + " (" + getInitials(name) + ") has overall score " + getOverallScore() + ".";
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
