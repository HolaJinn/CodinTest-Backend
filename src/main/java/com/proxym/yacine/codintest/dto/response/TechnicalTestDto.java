package com.proxym.yacine.codintest.dto.response;

import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Company;
import com.proxym.yacine.codintest.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechnicalTestDto {

    private Long id;
    private AppUser creator;
    private String title;
    private String description;
    private Integer timerInMinute;
    private Long companyId;
    private List<ExerciseDto> exercises;
    private String createdDate;
    private String lastModifiedDate;
}
