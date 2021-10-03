package com.example.vavaplanit.dto.mappers;

import com.example.vavaplanit.dto.weather.DailyWeatherDTO;
import com.example.vavaplanit.dto.weather.WeatherDTO;
import com.example.vavaplanit.model.weather.DailyWeather;
import com.example.vavaplanit.model.weather.Weather;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface WeatherDTOmapper {

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<DailyWeatherDTO> dailyWeatherListToDailyWeatherDTOlist(List<DailyWeather> dailyWeatherList);

    @Mappings({
            @Mapping(target = "date", source = "dt"),
            @Mapping(target = "minTemperature", source = "temp.min"),
            @Mapping(target = "maxTemperature", source = "temp.max"),
    })
    DailyWeatherDTO dailyWeatherToDailyWeatherDTO(DailyWeather dailyWeather);

    WeatherDTO weatherToWeatherDTO(Weather weather);
}
