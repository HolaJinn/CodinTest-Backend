package com.proxym.yacine.codintest.dto;

import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import com.proxym.yacine.codintest.model.Tag;
import com.proxym.yacine.codintest.util.ExerciseDifficulty;
import com.proxym.yacine.codintest.util.ExerciseStatus;
import com.proxym.yacine.codintest.util.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseFilterOption {

    private Integer page; //page number
    private Integer limit; // page size limit
    private Order order;  //sort order

    private String[] properties; //Order by

    private String title;
    private ExerciseDifficulty difficulty;
    private ExerciseStatus status;
    private Integer timerInMinute;
    private String programmingLanguage;
    private List<Long> tags;


}
