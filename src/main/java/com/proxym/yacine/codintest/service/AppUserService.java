package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.*;
import com.proxym.yacine.codintest.dto.response.AuthenticationResponse;
import com.proxym.yacine.codintest.model.AppUser;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AppUserService {
    List<AppUser> getAll();
    AppUser findByEmail(String email);
    AppUser findById(Long id);
    void saveAppUser(NewAppUser newAppUser, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void saveCompanyOwner(NewCompanyOwner newCompanyOwner, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void saveRecruiter(NewRecruiter newRecruiter, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void sendVerificationCode(NewAppUser newAppUser, String siteURL, String code) throws MessagingException, UnsupportedEncodingException;
    void sendResetPasswordToken(String email, String token, String siteURL) throws MessagingException, UnsupportedEncodingException;
    boolean verify(String verificationCode);
    AuthenticationResponse login(AuthenticationRequest request);
    AppUser getCurrentAuthenticatedUser();
    void updateResetPasswordToken(ForgotPasswordRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException;
    boolean verifyResetPasswordToken(String verificationCode);
    void updatePassword(String token, NewPasswordRequest request);
}
