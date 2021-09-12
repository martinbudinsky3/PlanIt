package com.example.vavaplanit.model.dto.mappers;

import com.example.vavaplanit.model.dto.DailyWeatherDTO;
import com.example.vavaplanit.model.weather.DailyWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DailyWeatherDTOmapper {

    @Mappings({
            @Mapping(target = "date", source = "dt"),
            @Mapping(target = "minTemperature", source = "temp.min"),
            @Mapping(target = "maxTemperature", source = "temp.max"),
    })
    DailyWeatherDTO dailyWeatherToDailyWeatherDTO(DailyWeather daily);
}
