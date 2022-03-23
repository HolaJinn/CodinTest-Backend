package com.proxym.yacine.codintest.dto.request;

import com.proxym.yacine.codintest.util.ExerciseDifficulty;
import com.proxym.yacine.codintest.util.ExerciseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
