package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
