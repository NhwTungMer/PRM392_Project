package model;

public class Goal {
    private String currentWeight;
    private String desiredWeight;
    private String duration;

    public Goal() {
    }

    public Goal(String currentWeight, String desiredWeight, String duration) {
        this.currentWeight = currentWeight;
        this.desiredWeight = desiredWeight;
        this.duration = duration;
    }

    public String getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getDesiredWeight() {
        return desiredWeight;
    }

    public void setDesiredWeight(String desiredWeight) {
        this.desiredWeight = desiredWeight;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
