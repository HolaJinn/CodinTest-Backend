package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long>, QuerydslPredicateExecutor<AppUser> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    AppUser findByVerificationCode(String verificationCode);
    AppUser findByResetPasswordToken(String resetPasswordToken);
    List<AppUser> findAllByCompany(Company company);
}
