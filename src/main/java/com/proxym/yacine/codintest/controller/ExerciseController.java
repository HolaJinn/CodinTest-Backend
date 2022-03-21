package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import com.proxym.yacine.codintest.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("")
    public ResponseEntity<?> createExercise(@RequestBody NewExerciseRequest newExerciseRequest) {
        exerciseService.create(newExerciseRequest);
        return new ResponseEntity<>("New Exercise is created", HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<ExerciseDto>> getAll() {
        List<ExerciseDto> exercises = exerciseService.getAll();
        return new ResponseEntity<>(exercises, HttpStatus.OK);
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

    @PostMapping("/test-cases/{id}")
    public ResponseEntity<?> addTestCase(@RequestBody NewTestCaseRequest newTestCaseRequest, @PathVariable Long id) {
        exerciseService.addTestCase(id, newTestCaseRequest);
        return new ResponseEntity<>("New test case is added to the exercise", HttpStatus.CREATED);
    }
}
