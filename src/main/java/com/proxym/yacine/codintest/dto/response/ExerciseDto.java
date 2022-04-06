package com.proxym.yacine.codintest.dto.response;

import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.Company;
import com.proxym.yacine.codintest.model.Exercise;
import com.proxym.yacine.codintest.model.Tag;
import com.proxym.yacine.codintest.model.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String programmingLanguageName;
    private String initialCode;
    private Long companyId;
    private List<TestCase> testCases;
    private List<Tag> tags;
    private String createdDate;
    private String lastModifiedDate;
}
