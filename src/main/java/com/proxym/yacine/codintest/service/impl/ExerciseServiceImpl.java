package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.*;
import com.proxym.yacine.codintest.repository.ExerciseRepository;
import com.proxym.yacine.codintest.repository.ProgrammingLanguageRepository;
import com.proxym.yacine.codintest.repository.TagRepository;
import com.proxym.yacine.codintest.repository.TestCaseRepository;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
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
    private TagRepository tagRepository;

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

        AppUser user = appUserService.getCurrentAuthenticatedUser();
        System.out.println(user.getRole());
        System.out.println(user.getRole().getId());
        System.out.println(user.getRole().getName());

        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }

        if (user.getRole().getId().intValue() == 4) {
            throw new CustomException("You are not allowed to create exercises", "UNAUTHORIZED", 403);
        }


//        AppUser appUser = appUserService.findById(newExerciseRequest.getCreatorId());
        ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByName(newExerciseRequest.getProgrammingLanguage())
                .orElseThrow(() -> new CustomException("No programming language found with this name", "PROGRAMMING LANGUAGE NOT FOUND", 404));
        Exercise exercise = Exercise.builder()
                .creator(user)
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
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));
        List<TestCase> testCases = exercise.getTestCases();
        return testCases.stream().map(testCase -> modelMapper.map(testCase, TestCaseResponse.class)).collect(Collectors.toList());
    }

    @Override
    public void addTestCase(NewTestCaseRequest newTestCaseRequest) {
        Exercise exercise = exerciseRepository.findById(newTestCaseRequest.getExerciseId())
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

    @Override
    public List<TagResponse> getAllTags(Long exerciseId) {
        Exercise exercise = exerciseRepository.getById(exerciseId);
        Collection<Tag> tags = exercise.getTags();
        return tags.stream().map(tag -> modelMapper.map(tag, TagResponse.class)).collect(Collectors.toList());
    }

    @Override
    public void addTag(Long exerciseId, Long tagId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if(!tagRepository.existsById(tagId)) {
            throw new CustomException("No tag found with such ID", "TAG NOT FOUND", 404);
        }

        Tag tag = tagRepository.getById(tagId);
        exercise.getTags().add(tag);
        exerciseRepository.save(exercise);
        log.info(String.format("Tag %s added successfully to exercise with ID: %s", tag.getName(), exerciseId));
    }
}
