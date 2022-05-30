package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Exercise;
import com.proxym.yacine.codintest.model.TechnicalTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface TechnicalTestRepository extends JpaRepository<TechnicalTest, Long>, QuerydslPredicateExecutor<TechnicalTest> {
    List<TechnicalTest> findTechnicalTestsByExercises(Exercise exercise);
}
