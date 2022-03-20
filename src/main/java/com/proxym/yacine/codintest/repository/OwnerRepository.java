package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
