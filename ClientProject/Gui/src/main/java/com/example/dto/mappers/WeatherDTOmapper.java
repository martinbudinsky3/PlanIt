package com.example.dto.mappers;

import com.example.dto.weather.DailyWeatherDTO;
import com.example.model.weather.DailyWeather;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class WeatherDTOmapper {
    private ModelMapper modelMapper = new ModelMapper();

    public List<DailyWeather> dailyWeatherDTOToDailyWeather(List<DailyWeatherDTO> dailyWeatherDTOlist) {
        return dailyWeatherDTOlist
                .stream()
                .map(dailyWeatherDTO -> modelMapper.map(dailyWeatherDTO, DailyWeather.class))
                .collect(Collectors.toList());
    }
}
