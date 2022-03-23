package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.ExerciseFilterOption;
import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDto> getAll();
    ExerciseDto findById(Long id);
    void create(NewExerciseRequest newExerciseRequest);
    List<TestCaseResponse> getAllTestCases(Long exerciseId);
    void addTestCase(NewTestCaseRequest newTestCaseRequest);
    List<TagResponse> getAllTags(Long exerciseId);
    void addTag(Long exerciseId, Integer tagId);
    Page<ExerciseDto> findAll(ExerciseFilterOption options);
}
