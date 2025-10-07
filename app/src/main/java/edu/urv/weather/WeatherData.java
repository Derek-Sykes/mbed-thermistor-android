package edu.urv.weather;

public class WeatherData {
    private final String date;
    private final String description;
    private final String temperature;

    public WeatherData(String date, String description, String temperature) {
        this.date = date;
        this.description = description;
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTemperature() {
        return temperature;
    }
}
