package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.ExerciseFilterOption;
import com.proxym.yacine.codintest.dto.request.NewExerciseRequest;
import com.proxym.yacine.codintest.dto.request.NewInitialCodeForExercise;
import com.proxym.yacine.codintest.dto.request.NewTestCaseRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.dto.response.TestCaseResponse;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.*;
import com.proxym.yacine.codintest.repository.*;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.ExerciseService;
import com.proxym.yacine.codintest.util.ExerciseDifficulty;
import com.proxym.yacine.codintest.util.ExerciseStatus;
import com.proxym.yacine.codintest.util.Order;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.proxym.yacine.codintest.model.QTag.tag;

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
    private CompanyRepository companyRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ExerciseDto findById(Long id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(
                () -> new CustomException("No Exercise found with such ID", "EXERCISE NOT FOUND", 400));
        return modelMapper.map(exercise, ExerciseDto.class);
    }

    @Override
    public ExerciseDto create(NewExerciseRequest newExerciseRequest) {

        AppUser user = appUserService.getCurrentAuthenticatedUser();

        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }

        if (user.getRole().getId().intValue() == 4) {
            throw new CustomException("You are not allowed to create exercises", "UNAUTHORIZED", 403);
        }

        Exercise exercise = Exercise.builder()
                .creator(user)
                .title(newExerciseRequest.getTitle())
                .description(newExerciseRequest.getDescription())
                .difficulty(newExerciseRequest.getDifficulty())
                .status(newExerciseRequest.getStatus())
                .timerInMinute(newExerciseRequest.getTimerInMinute())
                .company(user.getCompany())
                .build();

        List<Tag> tags = new ArrayList<>();
        List<Integer> tagsId = newExerciseRequest.getTags();
        tagsId.stream()
                .map(tagId -> {
                    Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new CustomException("There is no tag with such ID", "TAG NOT FOUND", 404));
                    tags.add(tag);
                    return tags;
                }).collect(Collectors.toList());
        exercise.setTags(tags);
        exerciseRepository.save(exercise);
        return modelMapper.map(exercise, ExerciseDto.class);
    }

    @Override
    public void updateExercise(Long exerciseId, Map<String, Object> changes) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if(exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }
        try {
            changes.forEach((key, value) -> {
                switch (key) {
                    case "title" : {
                        exercise.setTitle((String) value);
                        break;
                    }
                    case "description": {
                        exercise.setDescription((String) value);
                        break;
                    }
                    case "initialCode": {
                        exercise.setInitialCode((String) value);
                        break;
                    }
                    case "programmingLanguage" : {
                        ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByName((String) value)
                                .orElseThrow(() -> new CustomException("There is no programming language with such name", "PROGRAMMING LANGUAGE NOT FOUND", 404));
                        exercise.setProgrammingLanguage(programmingLanguage);
                        break;
                    }
                    case "timerInMinute": {
                        exercise.setTimerInMinute((Integer) value);
                        break;
                    }
                    case "difficulty": {
                        if (value.toString().equals(ExerciseDifficulty.Easy.name())) {
                            exercise.setDifficulty(ExerciseDifficulty.Easy);
                        } else if (value.toString().equals(ExerciseDifficulty.Medium.name())) {
                            exercise.setDifficulty(ExerciseDifficulty.Medium);
                        } else if (value.toString().equals(ExerciseDifficulty.Hard.name())) {
                            exercise.setDifficulty(ExerciseDifficulty.Hard);
                        }
                        break;
                    }
                    case "status" : {
                        if (value.toString().equals(ExerciseStatus.Public.name())) {
                            exercise.setStatus(ExerciseStatus.Public);
                        } else if(value.toString().equals(ExerciseStatus.Private.name())) {
                            exercise.setStatus(ExerciseStatus.Public);
                        }
                        break;
                    }
                }
            });
            exerciseRepository.save(exercise);
            log.info(String.format("Exercise with ID %s is updated successfully", exerciseId));
        } catch (Exception e) {
            throw new CustomException("Can't update with invalid resource", e.getMessage(), 400);
        }
    }

    @Override
    public void deleteExercise(Long exerciseId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if(exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }
        exerciseRepository.delete(exercise);
        log.info(String.format("Exercise with ID %s is deleted successfully", exerciseId));
    }

    @Override
    public List<TestCaseResponse> getAllTestCases(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));
        List<TestCase> testCases = exercise.getTestCases();
        return testCases.stream().map(testCase -> modelMapper.map(testCase, TestCaseResponse.class)).collect(Collectors.toList());
    }

    @Override
    public TestCaseResponse addTestCase(NewTestCaseRequest newTestCaseRequest) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();

        Exercise exercise = exerciseRepository.findById(newTestCaseRequest.getExerciseId())
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if(exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }
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
        return modelMapper.map(testCase, TestCaseResponse.class);
    }

    @Override
    public void updateTestCase(Long exerciseId, Long testCaseId, Map<String, Object> changes) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if(exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }

        TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(() -> new CustomException("There is no test case with such ID", "TEST CASE NOT FOUND", 404));

        try{
            changes.forEach((key, value) ->  {
                switch (key) {
                    case "name": {
                        testCase.setName((String) value);
                        break;
                    }
                    case "score": {
                        testCase.setScore(Long.valueOf((Integer) value));
                        break;
                    }
                    case "input": {
                        testCase.setInput((String) value);
                        break;
                    }
                    case "expectedOutput": {
                        testCase.setExpectedOutput((String) value);
                        break;
                    }
                    case "isSample": {
                        testCase.setSample((Boolean) value);
                        break;
                    }
                }
            });
            testCaseRepository.save(testCase);
            log.info(String.format("Test case with ID %s is successfully updated", testCaseId));
        } catch (Exception e) {
            throw new CustomException("Something went wrong", e.getMessage(), 400);
        }
    }

    @Override
    public void deleteTestCase(Long exerciseId, Long testCaseId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if(exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }

        TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(() -> new CustomException("There is no test case with such ID", "TEST CASE NOT FOUND", 404));
        if (!exercise.getTestCases().contains(testCase)) {
            throw new CustomException("This test case doesn't exist on that exercise", "TEST CASE NOT FOUND", 404);
        } else {
            testCaseRepository.delete(testCase);
            exercise.getTestCases().remove(testCase);
            exerciseRepository.save(exercise);
            log.info(String.format("Test case %s removed successfully from exercise with ID: %s", testCaseId, exerciseId));
        }
    }

    @Override
    public List<TagResponse> getAllTags(Long exerciseId) {
        Exercise exercise = exerciseRepository.getById(exerciseId);
        Collection<Tag> tags = exercise.getTags();
        return tags.stream().map(tag -> modelMapper.map(tag, TagResponse.class)).collect(Collectors.toList());
    }

    @Override
    public void addTag(Long exerciseId, Integer tagId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if (exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }

        if(!tagRepository.existsById(tagId)) {
            throw new CustomException("No tag found with such ID", "TAG NOT FOUND", 404);
        }

        Tag tag = tagRepository.getById(tagId);
        exercise.getTags().add(tag);
        exerciseRepository.save(exercise);
        log.info(String.format("Tag %s added successfully to exercise with ID: %s", tag.getName(), exerciseId));
    }

    @Override
    public void deleteTag(Long exerciseId, Integer tagId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if (exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }

        if(!tagRepository.existsById(tagId)) {
            throw new CustomException("No tag found with such ID", "TAG NOT FOUND", 404);
        }
        Tag tag = tagRepository.getById(tagId);
        if(!exercise.getTags().contains(tag)) {
            throw new CustomException("This tag does not exist on that exercise", "TAG NOT FOUND", 404);
        } else {
            exercise.getTags().remove(tag);
            exerciseRepository.save(exercise);
            log.info(String.format("Tag %s removed successfully from exercise with ID: %s", tag.getName(), exerciseId));
        }
    }

    @Override
    public void addInitialCode(NewInitialCodeForExercise newInitialCodeForExercise) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        Exercise exercise = exerciseRepository.findById(newInitialCodeForExercise.getExerciseId())
                .orElseThrow(() -> new CustomException("No exercise found with such ID", "EXERCISE NOT FOUND", 404));

        if(exercise.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this exercise!", "UNAUTHORIZED", 403);
        }

        ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByName(newInitialCodeForExercise.getProgrammingLanguage())
                .orElseThrow(() -> new CustomException("No programming language found with this name", "PROGRAMMING LANGUAGE NOT FOUND", 404));

        exercise.setProgrammingLanguage(programmingLanguage);
        exercise.setInitialCode(newInitialCodeForExercise.getInitialCode());
        exerciseRepository.save(exercise);
    }

    /**
     * This method allows receiving pages of exercises with filters optionally
     */
    @Override
    public Page<ExerciseDto> findAll(Map<String, Object> options) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        BooleanBuilder builder = new BooleanBuilder();
        final QExercise qExercise = QExercise.exercise;
        return doYourJob(qExercise, builder, options, user);
    }

    private Page<ExerciseDto> doYourJob(QExercise qExercise, BooleanBuilder builder, Map<String, Object> options, AppUser user) {
        int page = 0, limit = 10;
        builder.and(qExercise.company.id.eq(user.getCompany().getId()));
        if(options == null) {
            return exerciseRepository.findAll(builder, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "CreatedDate", "id")))
                    .map(exercise -> modelMapper.map(exercise, ExerciseDto.class));
        } else {
            page = (options.get("page") != null) ? Integer.parseInt((String) options.get("page")) : page;
            limit = (options.get("limit") != null) ? Integer.parseInt((String) options.get("limit")) : limit;
            if(options.get("title") != null) builder.and(qExercise.title.containsIgnoreCase((String)options.get("title")));
            if(options.get("difficulty") != null) builder.and(qExercise.difficulty.eq(ExerciseDifficulty.valueOf((String) options.get("difficulty"))));
            if(options.get("status") != null) {
                if(options.get("status").toString().equalsIgnoreCase("PRIVATE")) {
                    builder.and(qExercise.company.id.eq(user.getCompany().getId()));

                } else if(options.get("status").toString().equalsIgnoreCase("PUBLIC")) {
                    builder.and(qExercise.status.eq(ExerciseStatus.valueOf((String) options.get("status"))));
                }
            }

            //TODO check if this one works fine
//            else {
//                builder.and(qExercise.status.eq(ExerciseStatus.PUBLIC));
//                builder.and(qExercise.company.id.eq(user.getCompany().getId()));
//            }

            if(options.get("timerInMinute") != null) builder.and(qExercise.timerInMinute.between(0, Integer.valueOf((String) options.get("timerInMinute"))));
            if (options.get("programmingLanguage") != null) builder.and(qExercise.programmingLanguage.name.containsIgnoreCase((String) options.get("programmingLanguage")));
            if(options.get("tags") != null) {
                String[] tags = options.get("tags").toString().split(",");
                for(int i = 0; i < tags.length; i++) {
                    builder.and(qExercise.tags.any().id.eq(Math.toIntExact(Integer.parseInt(tags[i]))));
                }
            }
            if(options.get("createdByMe") != null){
                if(options.get("createdByMe").toString().equals("true")) {
                    builder.and(qExercise.creator.id.eq(user.getId()));
                }
            }
            Sort.Direction direction = (options.get("order").equals(Order.DESC.name())) ? Sort.Direction.DESC : Sort.Direction.ASC;
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, ((String) options.get("properties"))));
            return (builder.getValue() != null) ? exerciseRepository.findAll(builder, pageRequest).map(exercise -> modelMapper.map(exercise, ExerciseDto.class)) : exerciseRepository.findAll(pageRequest).map(exercise -> modelMapper.map(exercise, ExerciseDto.class));
        }
    }
}
