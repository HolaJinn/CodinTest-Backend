package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}
