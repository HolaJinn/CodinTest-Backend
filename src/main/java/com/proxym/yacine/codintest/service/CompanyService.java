package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.dto.response.RecruiterDto;
import com.proxym.yacine.codintest.model.Company;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CompanyService {
    Company save(NewCompanyRequest newCompanyRequest);
    Page<RecruiterDto> getAllRecruiters(Map<String, Object> options);
}
