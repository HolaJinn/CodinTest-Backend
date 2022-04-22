package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.dto.response.CurrentUserDto;
import com.proxym.yacine.codintest.model.Company;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    Company save(NewCompanyRequest newCompanyRequest);
    Page<CurrentUserDto> getAllRecruiters(Map<String, Object> options);
}
