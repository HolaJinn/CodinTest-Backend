package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface InvitationRepository extends JpaRepository<Invitation, Long>, QuerydslPredicateExecutor<Invitation> {
}
