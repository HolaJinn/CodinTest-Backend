package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.NewProgrammingLanguageRequest;
import com.proxym.yacine.codintest.dto.response.ProgrammingLanguageDto;
import com.proxym.yacine.codintest.service.ProgrammingLanguageService;
import com.proxym.yacine.codintest.util.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(Routes.programmingLanguagesRoute)
public class ProgrammingLanguageController {

    @Autowired
    private ProgrammingLanguageService service;

    @GetMapping("")
    public ResponseEntity<List<ProgrammingLanguageDto>> getAll() {
        List<ProgrammingLanguageDto> programmingLanguages = service.getAll();
        return new ResponseEntity<>(programmingLanguages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgrammingLanguageDto> getById(@PathVariable Long id) {
        ProgrammingLanguageDto programmingLanguage = service.findById(id);
        return new ResponseEntity<>(programmingLanguage, HttpStatus.OK);
    }

    @GetMapping(Routes.programmingLanguagesByNameRoute)
    public ResponseEntity<ProgrammingLanguageDto> getByName(@RequestParam String name) {
        ProgrammingLanguageDto programmingLanguage = service.findByName(name);
        return new ResponseEntity<>(programmingLanguage, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody NewProgrammingLanguageRequest programmingLanguage) {
        service.create(programmingLanguage);
        return new ResponseEntity<>("New Programming Language is created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>("Programming Language deleted successfully", HttpStatus.OK);
    }
}
