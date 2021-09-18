package com.example.vavaplanit.model.dto.mappers;

import com.example.vavaplanit.model.dto.weather.DailyWeatherDTO;
import com.example.vavaplanit.model.dto.weather.WeatherDTO;
import com.example.vavaplanit.model.weather.DailyWeather;
import com.example.vavaplanit.model.weather.Weather;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DailyWeatherDTOmapper {

    @IterableMapping
    @Mappings({
            @Mapping(target = "date", source = "dt"),
            @Mapping(target = "minTemperature", source = "temp.min"),
            @Mapping(target = "maxTemperature", source = "temp.max"),
    })
    List<DailyWeatherDTO> dailyWeatherToDailyWeatherDTO(Collection<DailyWeather> dailyWeatherCollection);

    WeatherDTO weatherToWeatherDTO(Weather weather);
}
