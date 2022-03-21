package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
