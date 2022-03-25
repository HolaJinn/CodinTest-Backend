package com.proxym.yacine.codintest.dto;

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
public class TechnicalTestFilterOption {
    private Integer page; //page number
    private Integer limit; // page size limit
    private Order order;  //sort order

    private String[] properties; //Order by

    private String title;
    private Integer timerInMinute;
    private Boolean createdByMe;
}
