package com.example.dto.weather;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;

public class DailyWeatherDTO {
    private LocalDate date;
    private int minTemperature;
    private int maxTemperature;
    private List<WeatherDTO> weather;

    public DailyWeatherDTO() {
    }

    public DailyWeatherDTO(LocalDate date, int minTemperature, int maxTemperature, List<WeatherDTO> weather) {
        this.date = date;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.weather = weather;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public List<WeatherDTO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherDTO> weather) {
        this.weather = weather;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
