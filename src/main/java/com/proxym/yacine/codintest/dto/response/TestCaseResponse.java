package com.proxym.yacine.codintest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseResponse {

    private Long id;
    private Long exerciseId;
    private String name;
    private Long score;
    private boolean isSample;
    private String input;
    private String expectedOutput;
}
