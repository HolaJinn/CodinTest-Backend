package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.NewAnswerRequest;
import com.proxym.yacine.codintest.dto.response.ExecutionResultResponse;
import com.proxym.yacine.codintest.service.JudgeService;
import com.proxym.yacine.codintest.util.Routes;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(Routes.judgeRoute)
public class JudgeController {

    @Autowired
    private JudgeService judgeService;

    @PostMapping("/pass-exercise/{id}")
    public ResponseEntity<List<ExecutionResultResponse>> passExercise(@PathVariable Long id, @RequestBody NewAnswerRequest newAnswerRequest) throws JSONException {
        List<ExecutionResultResponse> executionResultResponse = judgeService.passExercise(id, newAnswerRequest);
        return new ResponseEntity<>(executionResultResponse, HttpStatus.OK);
    }
}
