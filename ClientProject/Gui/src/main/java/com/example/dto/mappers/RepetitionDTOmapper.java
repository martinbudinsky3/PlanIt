package com.example.dto.mappers;

import com.example.dto.repetition.*;
import com.example.model.repetition.MonthlyRepetition;
import com.example.model.repetition.Repetition;
import com.example.model.repetition.WeeklyRepetition;
import com.example.model.repetition.YearlyRepetition;
import org.modelmapper.ModelMapper;

public class RepetitionDTOmapper {
    private ModelMapper modelMapper = new ModelMapper();

    public RepetitionCreateDTO repetitionToRepetitionCreateDTO(Repetition repetition) {
        if(repetition instanceof YearlyRepetition) {
            return modelMapper.map(repetition, YearlyRepetitionCreateDTO.class);
        }

        if(repetition instanceof MonthlyRepetition) {
            return modelMapper.map(repetition, MonthlyRepetitionCreateDTO.class);
        }

        if(repetition instanceof WeeklyRepetition) {
            return modelMapper.map(repetition, WeeklyRepetitionCreateDTO.class);

        }

        return modelMapper.map(repetition, RepetitionCreateDTO.class);
    }

    public Repetition repetitionDetailDTOtoRepetition(RepetitionDetailDTO repetitionDetailDTO) {
        if(repetitionDetailDTO instanceof YearlyRepetitionDetailDTO) {
            return modelMapper.map(repetitionDetailDTO, YearlyRepetition.class);
        }

        if(repetitionDetailDTO instanceof MonthlyRepetitionDetailDTO) {
            return modelMapper.map(repetitionDetailDTO, MonthlyRepetition.class);
        }

        if(repetitionDetailDTO instanceof WeeklyRepetitionDetailDTO) {
            return modelMapper.map(repetitionDetailDTO, WeeklyRepetition.class);
        }

        return modelMapper.map(repetitionDetailDTO, Repetition.class);
    }
}
