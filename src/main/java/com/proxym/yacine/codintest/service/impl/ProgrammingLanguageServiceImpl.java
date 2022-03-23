package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewProgrammingLanguageRequest;
import com.proxym.yacine.codintest.dto.response.ProgrammingLanguageDto;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import com.proxym.yacine.codintest.repository.ProgrammingLanguageRepository;
import com.proxym.yacine.codintest.service.ProgrammingLanguageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgrammingLanguageServiceImpl implements ProgrammingLanguageService {

    @Autowired
    private ProgrammingLanguageRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProgrammingLanguageDto> getAll() {
        return repository.findAll().stream()
                .map(programmingLanguage -> modelMapper.map(programmingLanguage, ProgrammingLanguageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProgrammingLanguageDto findById(Integer id) {
        ProgrammingLanguage programmingLanguage = repository.findById(id).orElseThrow(
                () -> new CustomException("No programming language found with such ID", "PROGRAMMING LANGUAGE NOT FOUND", 404)
        );
        return modelMapper.map(programmingLanguage, ProgrammingLanguageDto.class);
    }

    @Override
    public ProgrammingLanguageDto findByName(String name) {
        ProgrammingLanguage programmingLanguage = repository.findByName(name).orElseThrow(
                () -> new CustomException("No Programming language found with such name", "PROGRAMMING LANGUAGE NOT FOUND", 404));
        return modelMapper.map(programmingLanguage, ProgrammingLanguageDto.class);
    }

    @Override
    public void create(NewProgrammingLanguageRequest newProgrammingLanguageRequest) {
        if(repository.existsByName(newProgrammingLanguageRequest.getName())) {
            throw new CustomException("This programming language exists already", "ALREADY EXIST", 400);
        }
        repository.save(modelMapper.map(newProgrammingLanguageRequest, ProgrammingLanguage.class));
        log.info(String.format("Programming language {name: %s} added successfully at %s", newProgrammingLanguageRequest.getName(), LocalDateTime.now().toString()));
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new CustomException("No programming language found with such ID", "PROGRAMMING LANGUAGE NOT FOUND", 404);
        }
        repository.deleteById(id);
    }
}
