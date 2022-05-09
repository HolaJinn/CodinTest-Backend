package com.proxym.yacine.codintest.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListExercisesRequest {
    private Long technicalTestId;
    private List<Long> exercises;
}
