package com.example.vavaplanit.dto.weather;

public class WeatherDTO {
    private Integer id;
    private String main;
    private String description;
    private String iconUri;

    public WeatherDTO() {
    }

    public WeatherDTO(Integer id, String main, String description, String iconUri) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.iconUri = iconUri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }
}
