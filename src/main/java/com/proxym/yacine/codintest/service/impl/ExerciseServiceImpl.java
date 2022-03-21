package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Exercise;
import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import com.proxym.yacine.codintest.model.TestCase;
import com.proxym.yacine.codintest.repository.ExerciseRepository;
import com.proxym.yacine.codintest.repository.ProgrammingLanguageRepository;
import com.proxym.yacine.codintest.repository.TestCaseRepository;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ExerciseDto> getAll() {

        return exerciseRepository.findAll().stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseDto findById(Long id) {
        Exercise exercise =exerciseRepository.findById(id).orElseThrow(
                () -> new CustomException("No Exercise found with such ID", "EXERCISE NOT FOUND", 400));
        return modelMapper.map(exercise, ExerciseDto.class);
    }

    @Override
    public void create(NewExerciseRequest newExerciseRequest) {

        AppUser appUser = appUserService.findById(newExerciseRequest.getCreatorId());
        ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByName(newExerciseRequest.getProgrammingLanguage())
                .orElseThrow(() -> new CustomException("No programming language found with this name", "PROGRAMMING LANGUAGE NOT FOUND", 404));
        Exercise exercise = Exercise.builder()
                .creator(appUser)
                .title(newExerciseRequest.getTitle())
                .description(newExerciseRequest.getDescription())
                .initialCode(newExerciseRequest.getInitialCode())
                .programmingLanguage(programmingLanguage)
                .difficulty(newExerciseRequest.getDifficulty())
                .status(newExerciseRequest.getStatus())
                .timerInMinute(newExerciseRequest.getTimerInMinute())
                .build();
        exerciseRepository.save(exercise);
    }

    @Override
    public List<TestCaseResponse> getAllTestCases(Long exerciseId) {
        return testCaseRepository.findAll().stream()
                .map(testCase -> modelMapper.map(testCase, TestCaseResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addTestCase(Long exerciseId, NewTestCaseRequest newTestCaseRequest) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        TestCase testCase = TestCase.builder()
                .exercise(exercise)
                .name(newTestCaseRequest.getName())
                .score(newTestCaseRequest.getScore())
                .isSample(newTestCaseRequest.getIsSample())
                .input(newTestCaseRequest.getInput())
                .expectedOutput(newTestCaseRequest.getExpectedOutput())
                .build();
        exercise.getTestCases().add(testCase);
        testCaseRepository.save(testCase);
        exerciseRepository.save(exercise);
    }
}
