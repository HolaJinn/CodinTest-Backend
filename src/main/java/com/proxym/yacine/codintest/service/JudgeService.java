package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewAnswerRequest;
import com.proxym.yacine.codintest.dto.response.ExecutionResultResponse;
import org.json.JSONException;

import java.util.List;

public interface JudgeService {
    List<ExecutionResultResponse> executeCodeForSubmission(Long exerciseId, NewAnswerRequest newAnswerRequest) throws JSONException;
    List<ExecutionResultResponse> executeCodeForSample(Long exerciseId, NewAnswerRequest newAnswerRequest) throws JSONException;
}
