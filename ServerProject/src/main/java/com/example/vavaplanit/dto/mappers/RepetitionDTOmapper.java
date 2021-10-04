package com.example.vavaplanit.dto.mappers;

import com.example.vavaplanit.dto.repetition.*;
import com.example.vavaplanit.model.repetition.MonthlyRepetition;
import com.example.vavaplanit.model.repetition.Repetition;
import com.example.vavaplanit.model.repetition.WeeklyRepetition;
import com.example.vavaplanit.model.repetition.YearlyRepetition;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RepetitionDTOmapper {
    @Named("repetitionCreateDTOtoRepetition")
    default Repetition repetitionCreateDTOtoRepetition(RepetitionCreateDTO repetitionCreateDTO) {
        if(repetitionCreateDTO instanceof YearlyRepetitionCreateDTO) {
            return yearlyRepetitionCreateDTOtoYearlyRepetition((YearlyRepetitionCreateDTO) repetitionCreateDTO);
        }

        if(repetitionCreateDTO instanceof MonthlyRepetitionCreateDTO) {
            return monthlyRepetitionCreateDTOtoMonthlyRepetition((MonthlyRepetitionCreateDTO) repetitionCreateDTO);
        }

        if(repetitionCreateDTO instanceof WeeklyRepetitionCreateDTO) {
            return weeklyRepetitionCreateDTOtoWeeklyRepetition((WeeklyRepetitionCreateDTO) repetitionCreateDTO);
        }

        return dailyRepetitionCreateDTOtoDailyRepetition(repetitionCreateDTO);
    }

    Repetition dailyRepetitionCreateDTOtoDailyRepetition(RepetitionCreateDTO repetitionCreateDTO);
    WeeklyRepetition weeklyRepetitionCreateDTOtoWeeklyRepetition(WeeklyRepetitionCreateDTO weeklyRepetitionCreateDTO);
    MonthlyRepetition monthlyRepetitionCreateDTOtoMonthlyRepetition(MonthlyRepetitionCreateDTO monthlyRepetitionCreateDTO);
    YearlyRepetition yearlyRepetitionCreateDTOtoYearlyRepetition(YearlyRepetitionCreateDTO yearlyRepetitionCreateDTO);

    @Named("repetitionToRepetitionDetailDTO")
    default RepetitionDetailDTO repetitiontoRepetitionDetailDTO(Repetition repetition) {
        if(repetition instanceof YearlyRepetition) {
            return yearlyRepetitiontoYearlyRepetitionDetailDTO((YearlyRepetition) repetition);
        }

        if(repetition instanceof MonthlyRepetition) {
            return monthlyRepetitiontoMonthlyRepetitionDetailDTO((MonthlyRepetition) repetition);
        }

        if(repetition instanceof WeeklyRepetition) {
            return weeklyRepetitiontoWeeklyRepetitionDetailDTO((WeeklyRepetition) repetition);
        }

        return dailyRepetitiontoDailyRepetitionDetailDTO(repetition);
    }

    RepetitionDetailDTO dailyRepetitiontoDailyRepetitionDetailDTO(Repetition repetition);
    WeeklyRepetitionDetailDTO weeklyRepetitiontoWeeklyRepetitionDetailDTO(WeeklyRepetition weeklyRepetition);
    MonthlyRepetitionDetailDTO monthlyRepetitiontoMonthlyRepetitionDetailDTO(MonthlyRepetition monthlyRepetition);
    YearlyRepetitionDetailDTO yearlyRepetitiontoYearlyRepetitionDetailDTO(YearlyRepetition yearlyRepetition);
}
