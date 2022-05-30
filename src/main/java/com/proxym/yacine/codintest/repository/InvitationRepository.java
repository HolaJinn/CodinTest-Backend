package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.Company;
import com.proxym.yacine.codintest.model.Invitation;
import com.proxym.yacine.codintest.model.TechnicalTest;
import com.proxym.yacine.codintest.util.InvitationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long>, QuerydslPredicateExecutor<Invitation> {
    List<Invitation> findInvitationByCandidateEmail(String candidateEmail);
    List<Invitation> findInvitationByState(InvitationState state);
    List<Invitation> findInvitationByCompanyId(Long id);
    List<Invitation> findInvitationsByTechnicalTest(TechnicalTest technicalTest);
}
