package com.proxym.yacine.codintest.repository;

import com.proxym.yacine.codintest.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    AppUser findByVerificationCode(String verificationCode);
    AppUser findByResetPasswordToken(String resetPasswordToken);
}
