package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.TechnicalTestFilterOption;
import com.proxym.yacine.codintest.dto.request.NewTechnicalTestRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TechnicalTestDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TechnicalTestService {

    Page<TechnicalTestDto> findAll(TechnicalTestFilterOption option);
    void create(NewTechnicalTestRequest newTechnicalTestRequest);
    void update(Long id, Map<String, Object> changes);
    void delete(Long id);
    List<ExerciseDto> getAllExercises(Long id);
    void addExercise(Long id, Long exerciseId);
    void addListExercise(Long id, List<Long> exercises);
    void removeExercise(Long id, Long exerciseId);
}
