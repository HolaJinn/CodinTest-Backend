package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewProgrammingLanguageRequest;
import com.proxym.yacine.codintest.dto.response.ProgrammingLanguageDto;

import java.util.List;

public interface ProgrammingLanguageService {

    List<ProgrammingLanguageDto> getAll();
    ProgrammingLanguageDto findById(Long id);
    ProgrammingLanguageDto findByName(String name);
    void create (NewProgrammingLanguageRequest newProgrammingLanguageRequest);
    void delete(Long id);
}
