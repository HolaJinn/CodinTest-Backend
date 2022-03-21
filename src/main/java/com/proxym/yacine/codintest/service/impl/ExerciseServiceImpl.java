package com.proxym.yacine.codintest.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.ProgrammingLanguageDto;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.Exercise;
import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import com.proxym.yacine.codintest.repository.ExerciseRepository;
import com.proxym.yacine.codintest.repository.ProgrammingLanguageRepository;
import com.proxym.yacine.codintest.service.ExerciseService;
import com.proxym.yacine.codintest.service.ProgrammingLanguageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgrammingLanguageService programmingLanguageService;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ExerciseDto> getAll() {

        return exerciseRepository.findAll().stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseDto findById(Long id) {
        Exercise exercise =exerciseRepository.findById(id).orElseThrow(
                () -> new CustomException("No Exercise found with such ID", "EXERCISE NOT FOUND", 400));
        return modelMapper.map(exercise, ExerciseDto.class);
    }

    @Override
    public void create(NewExerciseRequest newExerciseRequest) {
        ProgrammingLanguageDto programmingLanguageDto = programmingLanguageService.findByName(newExerciseRequest.getProgrammingLanguage());
        ProgrammingLanguage programmingLanguage = modelMapper.map(programmingLanguageDto, ProgrammingLanguage.class);
        Exercise exercise = modelMapper.map(newExerciseRequest, Exercise.class);
        exercise.setProgrammingLanguage(programmingLanguage);
        exerciseRepository.save(exercise);
    }
}
