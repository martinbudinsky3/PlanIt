package com.example.dto.mappers;

import com.example.dto.repetition.*;
import com.example.model.repetition.MonthlyRepetition;
import com.example.model.repetition.Repetition;
import com.example.model.repetition.WeeklyRepetition;
import com.example.model.repetition.YearlyRepetition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepetitionDTOmapper {
    default RepetitionCreateDTO repetitionToRepetitionCreateDTO(Repetition repetition) {
        if(repetition instanceof YearlyRepetition) {
            return yearlyRepetitionToYearlyRepetitionCreateDTO((YearlyRepetition) repetition);
        }

        if(repetition instanceof MonthlyRepetition) {
            return monthlyRepetitionToMonthlyRepetitionCreateDTO((MonthlyRepetition) repetition);
        }

        if(repetition instanceof WeeklyRepetition) {
            return weeklyRepetitionToWeeklyRepetitionCreateDTO((WeeklyRepetition) repetition);
        }

        return dailyRepetitionToDailyRepetitionCreateDTO(repetition);
    }

    RepetitionCreateDTO dailyRepetitionToDailyRepetitionCreateDTO(Repetition repetition);
    WeeklyRepetitionCreateDTO weeklyRepetitionToWeeklyRepetitionCreateDTO(WeeklyRepetition weeklyRepetition);
    MonthlyRepetitionCreateDTO monthlyRepetitionToMonthlyRepetitionCreateDTO(MonthlyRepetition monthlyRepetition);
    YearlyRepetitionCreateDTO yearlyRepetitionToYearlyRepetitionCreateDTO(YearlyRepetition yearlyRepetition);


    default RepetitionDetailDTO repetitionToRepetitionDetailDTO(Repetition repetition) {
        if(repetition instanceof YearlyRepetition) {
            return yearlyRepetitionToYearlyRepetitionDetailDTO((YearlyRepetition) repetition);
        }

        if(repetition instanceof MonthlyRepetition) {
            return monthlyRepetitionToMonthlyRepetitionDetailDTO((MonthlyRepetition) repetition);
        }

        if(repetition instanceof WeeklyRepetition) {
            return weeklyRepetitionToWeeklyRepetitionDetailDTO((WeeklyRepetition) repetition);
        }

        return dailyRepetitionToDailyRepetitionDetailDTO(repetition);
    }

    RepetitionDetailDTO dailyRepetitionToDailyRepetitionDetailDTO(Repetition repetition);
    WeeklyRepetitionDetailDTO weeklyRepetitionToWeeklyRepetitionDetailDTO(WeeklyRepetition weeklyRepetition);
    MonthlyRepetitionDetailDTO monthlyRepetitionToMonthlyRepetitionDetailDTO(MonthlyRepetition monthlyRepetition);
    YearlyRepetitionDetailDTO yearlyRepetitionToYearlyRepetitionDetailDTO(YearlyRepetition yearlyRepetition);
}
