package com.proxym.yacine.codintest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutionResultResponse {
    private String stdout;
    private String time;
    private String memory;
    private String stderr;
    private String token;
    private String compile_output;
    private String message;
    private ExercutionStatusResponse status;
}
