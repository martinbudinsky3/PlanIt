package com.example.dto.mappers;

import com.example.dto.weather.DailyWeatherDTO;
import com.example.dto.weather.WeatherDTO;
import com.example.model.weather.DailyWeather;
import com.example.model.weather.Weather;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface WeatherDTOmapper {

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<DailyWeather> dailyWeatherDTOToDailyWeather(Collection<DailyWeatherDTO> dailyWeatherDTOcollection);

    Weather weatherDTOtoWeather(WeatherDTO weather);
}
