package com.example.vavaplanit.service.mappers;

import com.example.vavaplanit.model.dto.DailyWeatherDTO;
import com.example.vavaplanit.model.weather.DailyWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = DailyWeather.class)
public interface DailyWeatherDTOmapper {
    DailyWeatherDTOmapper INSTANCE = Mappers.getMapper(DailyWeatherDTOmapper.class);

    @Mappings({
            @Mapping(target = "date", source = "dt"),
            @Mapping(target = "minTemperature", source = "temp.min"),
            @Mapping(target = "maxTemperature", source = "temp.max")
    })
    DailyWeatherDTO dailyWeatherToDailyWeatherDTO(DailyWeather daily);
}
