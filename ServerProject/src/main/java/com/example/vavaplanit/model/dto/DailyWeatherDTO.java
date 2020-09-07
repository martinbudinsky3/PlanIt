package com.example.vavaplanit.model.dto;

import com.example.vavaplanit.model.weather.Weather;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class DailyWeatherDTO {
    private LocalDate date;
    private int minTemperature;
    private int maxTemperature;
    private List<Weather> weather;

    public DailyWeatherDTO() {
    }

    public DailyWeatherDTO(Long date, double minTemperature, double maxTemperature, List<Weather> weather) {
        setDate(date);
        setMinTemperature(minTemperature);
        setMaxTemperature(maxTemperature);
        this.weather = weather;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = (int) Math.round(minTemperature);
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = (int) Math.round(maxTemperature);
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

    public void setDate(Long date) {
        this.date = LocalDate.ofInstant(Instant.ofEpochSecond(date),
                TimeZone.getDefault().toZoneId());;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeatherDTO that = (DailyWeatherDTO) o;
        return Double.compare(that.minTemperature, minTemperature) == 0 &&
                Double.compare(that.maxTemperature, maxTemperature) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(weather, that.weather);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, minTemperature, maxTemperature, weather);
    }
}
