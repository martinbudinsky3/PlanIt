package com.example.client.model.weather;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DailyWeather {
    private LocalDate date;
    private int minTemperature;
    private int maxTemperature;
    private List<Weather> weather = null;

    public DailyWeather() {
    }

    public DailyWeather(LocalDate date, int minTemperature, int maxTemperature, List<Weather> weather) {
        this.date = date;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.weather = weather;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
