package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgrammingLanguageRepository extends JpaRepository<ProgrammingLanguage, Integer> {

    Optional<ProgrammingLanguage> findByName(String name);
    boolean existsByName(String name);
}
