package com.proxym.yacine.codintest.dto.request;

import com.proxym.yacine.codintest.model.Company;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCompanyRequest {

    private String name;
    private String country;
    private String roleInCompany;
}
