package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.ExerciseFilterOption;
import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTagForExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.request.TestCaseExerciseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import com.proxym.yacine.codintest.service.ExerciseService;
import com.proxym.yacine.codintest.util.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(Routes.exercisesRoute)
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("")
    public ResponseEntity<ExerciseDto> createExercise(@RequestBody NewExerciseRequest newExerciseRequest) {
        ExerciseDto exercise = exerciseService.create(newExerciseRequest);
        return new ResponseEntity<>(exercise, HttpStatus.CREATED);
    }

    @GetMapping("")
    public Page<ExerciseDto> getAllExercises(@RequestBody ExerciseFilterOption options){
        return exerciseService.findAll(options);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDto> getById(@PathVariable Long id) {
        ExerciseDto exercise = exerciseService.findById(id);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable Long id, @RequestBody Map<String, Object> changes) {
        exerciseService.updateExercise(id, changes);
        return new ResponseEntity<>("Exercise updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return new ResponseEntity<>("Exercise deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{exerciseId}/test-cases")
    public ResponseEntity<List<TestCaseResponse>> getAllTestCases(@PathVariable Long exerciseId) {
        List<TestCaseResponse> testCases = exerciseService.getAllTestCases(exerciseId);
        return new ResponseEntity<>(testCases, HttpStatus.OK);
    }

    @PostMapping("/test-cases")
    public ResponseEntity<TestCaseResponse> addTestCase(@RequestBody NewTestCaseRequest newTestCaseRequest) {
        TestCaseResponse testCaseResponse = exerciseService.addTestCase(newTestCaseRequest);
        return new ResponseEntity<>(testCaseResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{exerciseId}/test-cases/{testCaseId}")
    public ResponseEntity<?> updateTestCase(@PathVariable Long exerciseId, @PathVariable Long testCaseId, @RequestBody Map<String, Object> changes){
        exerciseService.updateTestCase(exerciseId, testCaseId, changes);
        return new ResponseEntity<>("Test case is updated successefully", HttpStatus.OK);
    }

    @DeleteMapping("/{exerciseId}/test-cases/{testCaseId}")
    public ResponseEntity<?> deleteTestCase(@PathVariable Long exerciseId, @PathVariable Long testCaseId) {
        exerciseService.deleteTestCase(exerciseId, testCaseId);
        return new ResponseEntity<>("Test case is deleted from the exercise", HttpStatus.OK);
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TagResponse>> getAllTags(@PathVariable Long id) {
        List<TagResponse> tags = exerciseService.getAllTags(id);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping("/{exerciseId}/tags")
    public ResponseEntity<?> addTag(@PathVariable Long exerciseId,@RequestBody NewTagForExerciseRequest newTagForExerciseRequest) {
        exerciseService.addTag(exerciseId, newTagForExerciseRequest.getTagId());
        return new ResponseEntity<>("Tag is added to the exercise", HttpStatus.OK);
    }

    @DeleteMapping("/{exerciseId}/tags/{tagId}")
    public ResponseEntity<?> removeTag(@PathVariable Long exerciseId, @PathVariable Integer tagId) {
        exerciseService.deleteTag(exerciseId, tagId);
        return new ResponseEntity<>("Tag is deleted from the exercise", HttpStatus.OK);
    }
}
