package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDto> getAll();
    ExerciseDto findById(Long id);
    void create(NewExerciseRequest newExerciseRequest);
}
