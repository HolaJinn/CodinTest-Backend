package com.proxym.yacine.codintest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewInitialCodeForExercise {
    private Long exerciseId;
    private String programmingLanguage;
    private String initialCode;
}
