package com.proxym.yacine.codintest.dto.request;

import com.proxym.yacine.codintest.util.ExerciseDifficulty;
import com.proxym.yacine.codintest.util.ExerciseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Integer> tags;
}
