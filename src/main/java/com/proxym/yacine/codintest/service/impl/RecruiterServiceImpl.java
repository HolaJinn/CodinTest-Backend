package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.model.Recruiter;
import com.proxym.yacine.codintest.repository.RecruiterRepository;
import com.proxym.yacine.codintest.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public List<Recruiter> getAll() {
        return recruiterRepository.findAll();
    }

    @Override
    public Recruiter create(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }
}
