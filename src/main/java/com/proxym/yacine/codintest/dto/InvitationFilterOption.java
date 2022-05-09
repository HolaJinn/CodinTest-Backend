package com.proxym.yacine.codintest.dto;

import com.proxym.yacine.codintest.util.InvitationState;
import com.proxym.yacine.codintest.util.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvitationFilterOption {

    private Integer page; //page number
    private Integer limit; // page size limit
    private Order order;  //sort order

    private String[] properties; //Order by

    private String candidateEmail;
    private InvitationState state;
    private Boolean isRated;
}
