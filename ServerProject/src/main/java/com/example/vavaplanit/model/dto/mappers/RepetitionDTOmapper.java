package com.example.vavaplanit.model.dto.mappers;

import com.example.vavaplanit.model.dto.repetition.*;
import com.example.vavaplanit.model.repetition.MonthlyRepetition;
import com.example.vavaplanit.model.repetition.Repetition;
import com.example.vavaplanit.model.repetition.WeeklyRepetition;
import com.example.vavaplanit.model.repetition.YearlyRepetition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepetitionDTOmapper {
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


    default Repetition repetitionDetailDTOtoRepetition(RepetitionDetailDTO repetitionDetailDTO) {
        if(repetitionDetailDTO instanceof YearlyRepetitionDetailDTO) {
            return yearlyRepetitionDetailDTOtoYearlyRepetition((YearlyRepetitionDetailDTO) repetitionDetailDTO);
        }

        if(repetitionDetailDTO instanceof MonthlyRepetitionDetailDTO) {
            return monthlyRepetitionDetailDTOtoMonthlyRepetition((MonthlyRepetitionDetailDTO) repetitionDetailDTO);
        }

        if(repetitionDetailDTO instanceof WeeklyRepetitionDetailDTO) {
            return weeklyRepetitionDetailDTOtoWeeklyRepetition((WeeklyRepetitionDetailDTO) repetitionDetailDTO);
        }

        return dailyRepetitionDetailDTOtoDailyRepetition(repetitionDetailDTO);
    }

    Repetition dailyRepetitionDetailDTOtoDailyRepetition(RepetitionDetailDTO repetitionDetailDTO);
    WeeklyRepetition weeklyRepetitionDetailDTOtoWeeklyRepetition(WeeklyRepetitionDetailDTO weeklyRepetitionDetailDTO);
    MonthlyRepetition monthlyRepetitionDetailDTOtoMonthlyRepetition(MonthlyRepetitionDetailDTO monthlyRepetitionDetailDTO);
    YearlyRepetition yearlyRepetitionDetailDTOtoYearlyRepetition(YearlyRepetitionDetailDTO yearlyRepetitionDetailDTO);
}
