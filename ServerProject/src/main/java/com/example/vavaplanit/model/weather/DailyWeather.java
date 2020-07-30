package com.example.vavaplanit.model.weather;

import com.example.vavaplanit.model.weather.FeelsLike;
import com.example.vavaplanit.model.weather.Temp;
import com.example.vavaplanit.model.weather.Weather;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dt",
        "sunrise",
        "sunset",
        "temp",
        "feels_like",
        "pressure",
        "humidity",
        "dew_point",
        "wind_speed",
        "wind_deg",
        "weather",
        "clouds",
        "pop",
        "uvi"
})

public class DailyWeather {

    @JsonProperty("dt")
    private LocalDate dt;
    @JsonProperty("sunrise")
    private Integer sunrise;
    @JsonProperty("sunset")
    private Integer sunset;
    @JsonProperty("temp")
    private Temp temp;
    @JsonProperty("feels_like")
    private FeelsLike feelsLike;
    @JsonProperty("pressure")
    private Integer pressure;
    @JsonProperty("humidity")
    private Integer humidity;
    @JsonProperty("dew_point")
    private Double dewPoint;
    @JsonProperty("wind_speed")
    private Double windSpeed;
    @JsonProperty("wind_deg")
    private Integer windDeg;
    @JsonProperty("weather")
    private List<Weather> weather = null;
    @JsonProperty("clouds")
    private Integer clouds;
    @JsonProperty("pop")
    private Integer pop;
    @JsonProperty("uvi")
    private Double uvi;

    @JsonProperty("dt")
    public LocalDate getDt() {
        return dt;
    }

    @JsonProperty("dt")
    public void setDt(Long dt) {
        this.dt = LocalDate.ofInstant(Instant.ofEpochSecond(dt),
                TimeZone.getDefault().toZoneId());
    }

    @JsonProperty("sunrise")
    public Integer getSunrise() {
        return sunrise;
    }

    @JsonProperty("sunrise")
    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }

    @JsonProperty("sunset")
    public Integer getSunset() {
        return sunset;
    }

    @JsonProperty("sunset")
    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }

    @JsonProperty("temp")
    public Temp getTemp() {
        return temp;
    }

    @JsonProperty("temp")
    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    @JsonProperty("feels_like")
    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    @JsonProperty("feels_like")
    public void setFeelsLike(FeelsLike feelsLike) {
        this.feelsLike = feelsLike;
    }

    @JsonProperty("pressure")
    public Integer getPressure() {
        return pressure;
    }

    @JsonProperty("pressure")
    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    @JsonProperty("humidity")
    public Integer getHumidity() {
        return humidity;
    }

    @JsonProperty("humidity")
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    @JsonProperty("dew_point")
    public Double getDewPoint() {
        return dewPoint;
    }

    @JsonProperty("dew_point")
    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    @JsonProperty("wind_speed")
    public Double getWindSpeed() {
        return windSpeed;
    }

    @JsonProperty("wind_speed")
    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @JsonProperty("wind_deg")
    public Integer getWindDeg() {
        return windDeg;
    }

    @JsonProperty("wind_deg")
    public void setWindDeg(Integer windDeg) {
        this.windDeg = windDeg;
    }

    @JsonProperty("weather")
    public List<Weather> getWeather() {
        return weather;
    }

    @JsonProperty("weather")
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    @JsonProperty("clouds")
    public Integer getClouds() {
        return clouds;
    }

    @JsonProperty("clouds")
    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    @JsonProperty("pop")
    public Integer getPop() {
        return pop;
    }

    @JsonProperty("pop")
    public void setPop(Integer pop) {
        this.pop = pop;
    }

    @JsonProperty("uvi")
    public Double getUvi() {
        return uvi;
    }

    @JsonProperty("uvi")
    public void setUvi(Double uvi) {
        this.uvi = uvi;
    }

}
