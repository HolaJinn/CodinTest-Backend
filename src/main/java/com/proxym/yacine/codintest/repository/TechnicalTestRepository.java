package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.TechnicalTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TechnicalTestRepository extends JpaRepository<TechnicalTest, Long>, QuerydslPredicateExecutor<TechnicalTest> {
}
