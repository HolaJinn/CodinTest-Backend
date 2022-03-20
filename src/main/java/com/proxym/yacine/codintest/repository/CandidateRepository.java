package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    boolean existsByEmail(String email);
}
