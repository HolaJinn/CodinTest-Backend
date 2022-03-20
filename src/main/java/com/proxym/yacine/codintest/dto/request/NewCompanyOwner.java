package com.proxym.yacine.codintest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NewCompanyOwner extends NewAppUser{
    private String companyName;
    private String roleInCompany;
    private String country;
}
