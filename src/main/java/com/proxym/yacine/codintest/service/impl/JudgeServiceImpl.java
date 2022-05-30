package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewAnswerRequest;
import com.proxym.yacine.codintest.dto.response.ExecutionResultResponse;
import com.proxym.yacine.codintest.dto.response.TokenResponse;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.Answer;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Exercise;
import com.proxym.yacine.codintest.model.TestCase;
import com.proxym.yacine.codintest.repository.AnswerRepository;
import com.proxym.yacine.codintest.repository.ExerciseRepository;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.JudgeService;
import com.proxym.yacine.codintest.util.ExecutionResult;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private AnswerRepository answerRepository;
    @Override
    public List<ExecutionResultResponse> executeCodeForSubmission(Long exerciseId, NewAnswerRequest newAnswerRequest) throws JSONException {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));
        List<TestCase> testCases = exercise.getTestCases();
        Answer answer = Answer.builder()
                .code(newAnswerRequest.getCode())
                .exercise(exercise)
                .user(user)
                .build();
        boolean isCorrect = true;
        List<ExecutionResultResponse> results = new ArrayList<>();
        Long score = 0L;
        for(int i = 0; i < testCases.size(); i++) {
            testCases.get(i).getInput();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String uri = "http://localhost:2358/submissions";
            JSONObject submittedCode = new JSONObject();
            submittedCode.put("source_code", newAnswerRequest.getCode());
            submittedCode.put("language_id", newAnswerRequest.getProgrammingLanguage());
            submittedCode.put("stdin", testCases.get(i).getInput());
            submittedCode.put("expected_output", testCases.get(i).getExpectedOutput());
            HttpEntity<String> request =
                    new HttpEntity<String>(submittedCode.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();
            TokenResponse result = restTemplate.postForObject(uri, request,TokenResponse.class);
            ExecutionResultResponse executionResult = restTemplate.getForObject(uri + "/" + result.getToken(), ExecutionResultResponse.class);
            while (executionResult.getStatus().getId().equals("1") || executionResult.getStatus().getId().equals("2")) {
                executionResult = restTemplate.getForObject(uri + "/" + result.getToken(), ExecutionResultResponse.class);
            }
            if (executionResult.getStatus().getId().equals("3")) {
                score += testCases.get(i).getScore();
            }
            if (!executionResult.getStatus().getId().equals("3")) {
                isCorrect = false;
            }
            results.add(executionResult);
        }
        answer.setScore(score);
        if(isCorrect) {
            answer.setExecutionResult(ExecutionResult.Correct);
        } else {
            answer.setExecutionResult(ExecutionResult.Wrong);
        }
        answerRepository.save(answer);
        return results;
    }

    @Override
    public List<ExecutionResultResponse> executeCodeForSample(Long exerciseId, NewAnswerRequest newAnswerRequest) throws JSONException {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));
        List<TestCase> testCases = exercise.getTestCases().stream().filter(testCase -> testCase.isSample() == true).collect(Collectors.toList());
        boolean isCorrect = true;
        List<ExecutionResultResponse> results = new ArrayList<>();
        for(int i = 0; i < testCases.size(); i++) {
            testCases.get(i).getInput();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String uri = "http://localhost:2358/submissions";
            JSONObject submittedCode = new JSONObject();
            submittedCode.put("source_code", newAnswerRequest.getCode());
            submittedCode.put("language_id", newAnswerRequest.getProgrammingLanguage());
            submittedCode.put("stdin", testCases.get(i).getInput());
            submittedCode.put("expected_output", testCases.get(i).getExpectedOutput());
            HttpEntity<String> request =
                    new HttpEntity<String>(submittedCode.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();
            TokenResponse result = restTemplate.postForObject(uri, request,TokenResponse.class);
            ExecutionResultResponse executionResult = restTemplate.getForObject(uri + "/" + result.getToken(), ExecutionResultResponse.class);
            while (executionResult.getStatus().getId().equals("1") || executionResult.getStatus().getId().equals("2")) {
                executionResult = restTemplate.getForObject(uri + "/" + result.getToken(), ExecutionResultResponse.class);
            }
            if (!executionResult.getStatus().getId().equals("3")) {
                isCorrect = false;
            }
            results.add(executionResult);
        }
        return results;
    }
}
