package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
    boolean existsByName(String name);
}
