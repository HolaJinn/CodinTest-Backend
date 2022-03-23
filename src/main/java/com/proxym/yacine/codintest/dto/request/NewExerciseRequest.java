package com.proxym.yacine.codintest.dto.request;

import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.*;
import com.proxym.yacine.codintest.util.ExerciseDifficulty;
import com.proxym.yacine.codintest.util.ExerciseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewExerciseRequest {

    private String title;
    private String description;
    private ExerciseDifficulty difficulty;
    private ExerciseStatus status;
    private Integer timerInMinute;
    private String programmingLanguage;
    private String initialCode;

    //TODO change these two to their DTOs
//    private Collection<Tag> tags = new ArrayList<>();
}
