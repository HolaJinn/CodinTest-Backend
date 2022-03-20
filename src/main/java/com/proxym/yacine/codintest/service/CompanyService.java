package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.model.Company;

public interface CompanyService {
    Company save(NewCompanyRequest newCompanyRequest);
}
