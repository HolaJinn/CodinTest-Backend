package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.model.Company;
import com.proxym.yacine.codintest.repository.CompanyRepository;
import com.proxym.yacine.codintest.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company save(NewCompanyRequest newCompanyRequest) {
        return null;
    }
}
