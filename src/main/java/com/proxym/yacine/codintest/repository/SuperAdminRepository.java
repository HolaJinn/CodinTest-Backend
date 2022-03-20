package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {
    boolean existsByEmail(String email);
}
