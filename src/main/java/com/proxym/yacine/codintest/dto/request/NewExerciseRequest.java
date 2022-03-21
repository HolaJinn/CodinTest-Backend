package com.proxym.yacine.codintest.dto.request;

import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import com.proxym.yacine.codintest.model.Tag;
import com.proxym.yacine.codintest.model.TestCase;
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

    private Long creatorId;
    private String title;
    private String description;
    private String difficulty;
    private String status;
    private Integer timerInMinute;
    private String programmingLanguage;
    private String initialCode;

    //TODO change these two to their DTOs
//    private List<TestCase> testCases;
//    private Collection<Tag> tags = new ArrayList<>();
}
