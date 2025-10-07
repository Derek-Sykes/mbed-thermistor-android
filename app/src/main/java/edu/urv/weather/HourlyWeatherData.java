package edu.urv.weather;

public class HourlyWeatherData {
    private String time;
    private String temperature;
    private String description;

    public HourlyWeatherData(String time, String temperature, String description) {
        this.time = time;
        this.temperature = temperature;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }
}

