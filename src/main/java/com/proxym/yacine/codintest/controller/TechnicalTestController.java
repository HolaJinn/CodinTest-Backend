package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.TechnicalTestFilterOption;
import com.proxym.yacine.codintest.dto.request.ListExercisesRequest;
import com.proxym.yacine.codintest.dto.request.NewTechnicalTestRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TechnicalTestDto;
import com.proxym.yacine.codintest.service.TechnicalTestService;
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
@RequestMapping(Routes.technicalTestRoute)
public class TechnicalTestController {

    @Autowired
    private TechnicalTestService technicalTestService;

    @GetMapping("")
    public Page<TechnicalTestDto> findAll(@RequestParam Map<String, Object> options) {
        return technicalTestService.findAll(options);
    }

    @PostMapping("")
    public ResponseEntity<TechnicalTestDto> create(@RequestBody NewTechnicalTestRequest newTechnicalTestRequest) {
        TechnicalTestDto technicalTest = technicalTestService.create(newTechnicalTestRequest);
        return new ResponseEntity<>(technicalTest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> changes) {
        technicalTestService.update(id, changes);
        return new ResponseEntity<>("Technical test is updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        technicalTestService.delete(id);
        return new ResponseEntity<>("Technical test is deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}/exercises")
    public ResponseEntity<List<ExerciseDto>> getAllExercises(@PathVariable Long id) {
        List<ExerciseDto> exercises = technicalTestService.getAllExercises(id);
        return new ResponseEntity<>(exercises, HttpStatus.OK);
    }

    @PostMapping("/{id}/exercises/{exerciseId}")
    public ResponseEntity<?> addExercise(@PathVariable Long id, @PathVariable Long exerciseId) {
        technicalTestService.addExercise(id, exerciseId);
        return new ResponseEntity<>("Exercise added to technical test successfully", HttpStatus.OK);
    }

    @PostMapping("/exercises")
    public ResponseEntity<?> addListExercises(@RequestBody ListExercisesRequest list) {
        technicalTestService.addListExercise(list.getTechnicalTestId(), list.getExercises());
        return new ResponseEntity<>("Exercises are added to the technical test", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/exercises/{exerciseId}")
    public ResponseEntity<?> removeExercise(@PathVariable Long id, @PathVariable Long exerciseId) {
        technicalTestService.removeExercise(id,exerciseId);
        return new ResponseEntity<>("Exercise removed from technical test successfully", HttpStatus.OK);
    }
}
