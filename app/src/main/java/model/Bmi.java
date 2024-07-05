package model;

public class Bmi {
    private int weight;
    private int height;
    private double bmi;
    private String timestamp;
    private String bmiCategory;

    private String date;
    // Default constructor
    public Bmi() {}

    // Constructor with parameters
    public Bmi(double bmi, int height, int weight, String timestamp, String bmiCategory,String date) {
        this.bmi = bmi;
        this.height = height;
        this.weight = weight;
        this.timestamp = timestamp;
        this.bmiCategory = bmiCategory;
        this.date=date;
    }

    // Getters and setters
    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBmiCategory() {
        return bmiCategory;
    }

    public void setBmiCategory(String bmiCategory) {
        this.bmiCategory = bmiCategory;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
}
}

