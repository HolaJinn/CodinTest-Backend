package com.proxym.yacine.codintest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTestCaseRequest {

    private Long exerciseId;
    private String name;
    private Long score;
    private Boolean isSample;
    private String input;
    private String expectedOutput;
}
