package com.proxym.yacine.codintest.dto.response;

import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseDto {

    private Long id;
    private Long creatorId;
    private String title;
    private String description;
    private String difficulty;
    private String status;
    private Integer timerInMinute;
    private String programmingLanguage;
    private String initialCode;

    public static ExerciseDto fromEntity(Exercise exercise) {
        if(exercise == null) {
            throw new CustomException("Can't map null entity", "INVALID EXERCISE", 400);
        }
        return ExerciseDto.builder()
                .id(exercise.getId())
                .creatorId(exercise.getCreator().getId())
                .title(exercise.getTitle())
                .build();

    }
}
