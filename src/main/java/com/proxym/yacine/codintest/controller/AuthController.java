package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.*;
import com.proxym.yacine.codintest.dto.response.AuthenticationResponse;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody NewAppUser newAppUser, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.saveAppUser(newAppUser, Utility.getSiteURL(request));
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PostMapping("/owner/register")
    public ResponseEntity<?> registerOwner (@RequestBody NewCompanyOwner newCompanyOwner, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.saveCompanyOwner(newCompanyOwner, Utility.getSiteURL(request));
        return new ResponseEntity<>("New company owner added successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
        response = appUserService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("code") String code) {
        if (appUserService.verify(code)) {
            return new ResponseEntity<>("Verification success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Verification failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.updateResetPasswordToken(forgotPasswordRequest, Utility.getSiteURL(request));
        return new ResponseEntity<>("A verification link is sent to your email", HttpStatus.OK);
    }

    @GetMapping("/verify-reset-password")
    public ResponseEntity<?> verifyResetPassword(@RequestParam("token") String token) {
        if(appUserService.verifyResetPasswordToken(token)) {
            return new ResponseEntity<>("You can now change reset your password", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You are using invalid token", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody NewPasswordRequest request) {
        appUserService.updatePassword(token, request);
        return new ResponseEntity<>("Your password is changed", HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        AppUser appUser =  appUserService.getCurrentAuthenticatedUser();
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }

}
