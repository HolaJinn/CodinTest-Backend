package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.ExerciseFilterOption;
import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTagForExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import com.proxym.yacine.codintest.service.ExerciseService;
import com.proxym.yacine.codintest.util.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(Routes.exercisesRoute)
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("")
    public ResponseEntity<?> createExercise(@RequestBody NewExerciseRequest newExerciseRequest) {
        exerciseService.create(newExerciseRequest);
        return new ResponseEntity<>("New Exercise is created", HttpStatus.CREATED);
    }

//    public ResponseEntity<List<ExerciseDto>> getAll() {
//        List<ExerciseDto> exercises = exerciseService.getAll();
//        return new ResponseEntity<>(exercises, HttpStatus.OK);
//    }
    @GetMapping("")
    public Page<ExerciseDto> getAllExercises(@RequestBody ExerciseFilterOption options){
        return exerciseService.findAll(options);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDto> getById(@PathVariable Long id) {
        ExerciseDto exercise = exerciseService.findById(id);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }

    @GetMapping("/test-cases/{id}")
    public ResponseEntity<List<TestCaseResponse>> getAllTestCases(@PathVariable Long id) {
        List<TestCaseResponse> testCases = exerciseService.getAllTestCases(id);
        return new ResponseEntity<>(testCases, HttpStatus.OK);
    }

    @PostMapping("/test-cases")
    public ResponseEntity<?> addTestCase(@RequestBody NewTestCaseRequest newTestCaseRequest) {
        exerciseService.addTestCase(newTestCaseRequest);
        return new ResponseEntity<>("New test case is added to the exercise", HttpStatus.CREATED);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<List<TagResponse>> getAllTags(@PathVariable Long id) {
        List<TagResponse> tags = exerciseService.getAllTags(id);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping("/tags")
    public ResponseEntity<?> addTag(@RequestBody NewTagForExerciseRequest newTagForExerciseRequest) {
        exerciseService.addTag(newTagForExerciseRequest.getExerciseId(), newTagForExerciseRequest.getTagId());
        return new ResponseEntity<>("Tag is added to the exercise", HttpStatus.OK);
    }
}
