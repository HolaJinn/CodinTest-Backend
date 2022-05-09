package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.ExerciseFilterOption;
import com.proxym.yacine.codintest.dto.request.NewAnswerRequest;
import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewInitialCodeForExercise;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.response.ExecutionResultResponse;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import org.json.JSONException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ExerciseService {
    Page<ExerciseDto> findAll(Map<String, Object> options);
    ExerciseDto findById(Long id);
    ExerciseDto create(NewExerciseRequest newExerciseRequest);
    void updateExercise(Long exerciseId, Map<String, Object> changes);
    void deleteExercise(Long exerciseId);
    List<TestCaseResponse> getAllTestCases(Long exerciseId);
    TestCaseResponse addTestCase(NewTestCaseRequest newTestCaseRequest);
    void updateTestCase(Long exerciseId, Long testCaseId, Map<String, Object> changes);
    void deleteTestCase(Long exerciseId, Long testCaseId);
    List<TagResponse> getAllTags(Long exerciseId);
    void addTag(Long exerciseId, Integer tagId);
    void deleteTag(Long exerciseId, Integer tagId);
    void addInitialCode(NewInitialCodeForExercise newInitialCodeForExercise);
    ExecutionResultResponse passExercise(Long exerciseId, NewAnswerRequest newAnswerRequest) throws JSONException;
}
