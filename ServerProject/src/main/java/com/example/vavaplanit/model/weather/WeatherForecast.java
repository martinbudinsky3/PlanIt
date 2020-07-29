package com.example.vavaplanit.model.weather;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lat",
        "lon",
        "timezone",
        "timezone_offset",
        "daily"
})
public class WeatherForecast {

    @JsonProperty("lat")
    private Integer lat;
    @JsonProperty("lon")
    private Integer lon;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("timezone_offset")
    private Integer timezoneOffset;
    @JsonProperty("daily")
    private List<DailyWeather> daily = null;

    @JsonProperty("lat")
    public Integer getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(Integer lat) {
        this.lat = lat;
    }

    @JsonProperty("lon")
    public Integer getLon() {
        return lon;
    }

    @JsonProperty("lon")
    public void setLon(Integer lon) {
        this.lon = lon;
    }

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @JsonProperty("timezone_offset")
    public Integer getTimezoneOffset() {
        return timezoneOffset;
    }

    @JsonProperty("timezone_offset")
    public void setTimezoneOffset(Integer timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    @JsonProperty("daily")
    public List<DailyWeather> getDaily() {
        return daily;
    }

    @JsonProperty("daily")
    public void setDaily(List<DailyWeather> daily) {
        this.daily = daily;
    }

}
