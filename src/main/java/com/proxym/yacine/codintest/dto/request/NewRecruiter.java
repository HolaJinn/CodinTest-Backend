package com.proxym.yacine.codintest.dto.request;

import lombok.Data;

@Data
public class NewRecruiter extends NewAppUser{

    private String roleInCompany;
    private Long company;

}
