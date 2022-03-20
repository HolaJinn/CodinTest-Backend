package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.model.Recruiter;

import java.util.List;

public interface RecruiterService {

    List<Recruiter> getAll();
    Recruiter create(Recruiter recruiter);
}
