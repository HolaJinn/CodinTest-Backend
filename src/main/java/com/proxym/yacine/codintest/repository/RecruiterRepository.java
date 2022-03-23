package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    boolean existsByEmail(String email);
    Recruiter findByEmail(String email);
    List<Recruiter> findAllByCompanyId(Long companyId);
}
