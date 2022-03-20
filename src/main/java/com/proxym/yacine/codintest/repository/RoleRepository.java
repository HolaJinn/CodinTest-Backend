package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);
    Role findByName(String name);
}
