package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.TechnicalTestFilterOption;
import com.proxym.yacine.codintest.dto.request.NewTechnicalTestRequest;
import com.proxym.yacine.codintest.dto.response.ExerciseDto;
import com.proxym.yacine.codintest.dto.response.TechnicalTestDto;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Exercise;
import com.proxym.yacine.codintest.model.QTechnicalTest;
import com.proxym.yacine.codintest.model.TechnicalTest;
import com.proxym.yacine.codintest.repository.ExerciseRepository;
import com.proxym.yacine.codintest.repository.TechnicalTestRepository;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.TechnicalTestService;
import com.proxym.yacine.codintest.util.Order;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TechnicalTestServiceImpl implements TechnicalTestService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private TechnicalTestRepository technicalTestRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<TechnicalTestDto> findAll(Map<String, Object> options) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        BooleanBuilder builder = new BooleanBuilder();
        final QTechnicalTest qTechnicalTest = QTechnicalTest.technicalTest;
        return doYourJob(qTechnicalTest, builder, options, user);
    }

    @Override
    public TechnicalTestDto findById(Long id) {
        TechnicalTest technicalTest = technicalTestRepository.findById(id).orElseThrow(
                () -> new CustomException("No technical test found with such ID", "TECHNICAL TEST NOT FOUND", 400));
        return modelMapper.map(technicalTest, TechnicalTestDto.class);

    }

    private Page<TechnicalTestDto> doYourJob(QTechnicalTest qTechnicalTest, BooleanBuilder builder, Map<String, Object> options, AppUser user) {
        int page = 0, limit = 10;

        builder.and(qTechnicalTest.company.id.eq(user.getCompany().getId()));

        if (options == null) {
            return exerciseRepository.findAll(builder, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "CreatedDate", "id")))
                    .map(technicalTest -> modelMapper.map(technicalTest, TechnicalTestDto.class));
        } else {
            page = (options.get("page") != null) ? Integer.parseInt((String) options.get("page")) : page;
            limit = (options.get("limit") != null) ? Integer.parseInt((String) options.get("limit")): limit;
            if(options.get("title") != null) builder.and(qTechnicalTest.title.containsIgnoreCase((String)options.get("title")));
            if(options.get("timerInMinute") != null) builder.and(qTechnicalTest.timerInMinute.between(0, Integer.valueOf((String) options.get("timerInMinute"))));
            if (options.get("createdByMe") != null) {
                if (options.get("createdByMe").toString().equals("true")) {
                    builder.and(qTechnicalTest.creator.id.eq(user.getId()));
                }
            }
            Sort.Direction direction = (options.get("order").equals(Order.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, ((String) options.get("properties"))));
            return (builder.getValue() != null) ? technicalTestRepository.findAll(builder, pageRequest).map(technicalTest -> modelMapper.map(technicalTest, TechnicalTestDto.class)) : technicalTestRepository.findAll(pageRequest).map(technicalTest -> modelMapper.map(technicalTest, TechnicalTestDto.class));
        }
    }

    @Override
    public TechnicalTestDto create(NewTechnicalTestRequest newTechnicalTestRequest) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }

        if (user.getRole().getId().intValue() == 4) {
            throw new CustomException("You are not allowed to create technical tests", "UNAUTHORIZED", 403);
        }

        TechnicalTest technicalTest = TechnicalTest.builder()
                .creator(user)
                .title(newTechnicalTestRequest.getTitle())
                .description(newTechnicalTestRequest.getDescription())
                .timerInMinute(newTechnicalTestRequest.getTimerInMinute())
                .company(user.getCompany())
                .build();

        technicalTestRepository.save(technicalTest);
        log.info(String.format("New technical test %s is created", newTechnicalTestRequest.getTitle()));
        return modelMapper.map(technicalTest, TechnicalTestDto.class);
    }

    @Override
    public void update(Long id, Map<String, Object> changes) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();

        TechnicalTest technicalTest = technicalTestRepository.findById(id).orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));

        if(technicalTest.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this technical test!", "UNAUTHORIZED", 403);
        }

        try {
            changes.forEach((key, value) -> {
                switch (key) {
                    case "title": {
                        technicalTest.setTitle((String) value);
                        break;
                    }
                    case "description": {
                        technicalTest.setDescription((String) value);
                        break;
                    }
                    case "timerInMinute": {
                        technicalTest.setTimerInMinute((Integer) value);
                        break;
                    }
                }
            });
            technicalTestRepository.save(technicalTest);
            log.info(String.format("Technical test with ID %s is updated successfully", id));
        } catch (Exception e) {
            throw new CustomException("Something went wrong", e.getMessage(), 400);
        }
    }

    @Override
    public void delete(Long id) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();

        TechnicalTest technicalTest = technicalTestRepository.findById(id).orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));

        if(technicalTest.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this technical test!", "UNAUTHORIZED", 403);
        }
        technicalTestRepository.deleteById(id);
        log.info(String.format("Technical test with ID %s is deleted successfully", id));
    }

    @Override
    public List<ExerciseDto> getAllExercises(Long id) {
        TechnicalTest technicalTest = technicalTestRepository.findById(id).orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));
        Collection<Exercise> exercises = technicalTest.getExercises();
        return exercises.stream().map(exercise -> modelMapper.map(exercise, ExerciseDto.class)).collect(Collectors.toList());
    }

    @Override
    public void addExercise(Long id, Long exerciseId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        TechnicalTest technicalTest = technicalTestRepository.findById(id).orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));
        if (technicalTest.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this technical test!", "UNAUTHORIZED", 403);
        }
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new CustomException("There is no exercise with such ID", "EXERCISE NOT FOUND", 404));

        if (exercise.getCompany().getId() != user.getCompany().getId()) {
            throw new CustomException("This exercise doesn't exist on your company's virtual space!!", "BAD REQUEST", 400);
        }
        Collection<Exercise> exercises = technicalTest.getExercises();
        if (exercises.contains(exercise)) {
            throw new CustomException("This exercise exists already for this technical test", "ALREADY EXIST", 400);
        } else {
            exercises.add(exercise);
            technicalTest.setExercises(exercises);
            technicalTestRepository.save(technicalTest);
            log.info(String.format("Exercise with ID %s is added to the technical test with ID %s", exerciseId, id));
        }
    }

    @Override
    public void addListExercise(Long id, List<Long> exercises) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        TechnicalTest technicalTest = technicalTestRepository.findById(id).orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));
        if (technicalTest.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this technical test!", "UNAUTHORIZED", 403);
        }
        Collection<Exercise> existingExercises = technicalTest.getExercises();

        exercises.stream()
                .map(exerciseId -> {
                    Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new CustomException("There is no exercise with such ID", "EXERCISE NOT FOUND", 404));
                    if (exercise.getCompany().getId() != user.getCompany().getId()) {
                         throw new CustomException("This exercise doesn't exist on your company's virtual space!!", "BAD REQUEST", 400);
                    }
                    if (existingExercises.contains(exercise)) {
                        throw new CustomException("This exercise already exist on the technical test","ALREADY EXIST", 400);
                    } else {
                        existingExercises.add(exercise);
                    }
                    return existingExercises;
                })
                .collect(Collectors.toList());
        technicalTest.setExercises(existingExercises);
        technicalTestRepository.save(technicalTest);
        log.info(String.format("Exercises are added to the technical test with ID %s", id));

    }

    @Override
    public void removeExercise(Long id, Long exerciseId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        TechnicalTest technicalTest = technicalTestRepository.findById(id).orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));
        if (technicalTest.getCreator().getId() != user.getId()) {
            throw new CustomException("You are not the original creator of this technical test!", "UNAUTHORIZED", 403);
        }
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new CustomException("There is no exercise with such ID", "EXERCISE NOT FOUND", 404));

        if (exercise.getCompany().getId() != user.getCompany().getId()) {
            throw new CustomException("This exercise doesn't exist on your company's virtual space!!", "BAD REQUEST", 400);
        }
        Collection<Exercise> exercises = technicalTest.getExercises();
        if (!exercises.contains(exercise)) {
            throw new CustomException("This exercise doesn't exist on this technical test", "ALREADY EXIST", 400);
        } else {
            exercises.remove(exercise);
            technicalTest.setExercises(exercises);
            technicalTestRepository.save(technicalTest);
            log.info(String.format("Exercise with ID %s is removed from the technical test with ID %s", exerciseId, id));
        }
    }
}
