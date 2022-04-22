package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.*;
import com.proxym.yacine.codintest.dto.response.AuthenticationResponse;
import com.proxym.yacine.codintest.dto.response.CurrentUserDto;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.util.Routes;
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
@RequestMapping(Routes.authRoute)
public class AuthController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping(Routes.registerRoute)
    public ResponseEntity<?> register (@RequestBody NewAppUser newAppUser, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.saveAppUser(newAppUser, Utility.getSiteURL(request));
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PostMapping(Routes.registerOwnerRoute)
    public ResponseEntity<?> registerOwner (@RequestBody NewCompanyOwner newCompanyOwner, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.saveCompanyOwner(newCompanyOwner, Utility.getSiteURL(request));
        return new ResponseEntity<>("New company owner added successfully", HttpStatus.CREATED);
    }

    @PostMapping(Routes.loginRoute)
    public ResponseEntity<AuthenticationResponse> login (@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
        response = appUserService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(Routes.verifyAccountRoute)
    public ResponseEntity<?> verifyUser(@RequestParam("code") String code) {
        if (appUserService.verify(code)) {
            return new ResponseEntity<>("Verification success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Verification failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(Routes.forgotPasswordRoute)
    public ResponseEntity<?> processForgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.updateResetPasswordToken(forgotPasswordRequest, Utility.getSiteURL(request));
        return new ResponseEntity<>("A verification link is sent to your email", HttpStatus.OK);
    }

    @GetMapping(Routes.verifyResetPasswordRoute)
    public ResponseEntity<?> verifyResetPassword(@RequestParam("token") String token) {
        if(appUserService.verifyResetPasswordToken(token)) {
            return new ResponseEntity<>("You can now change reset your password", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You are using invalid token", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(Routes.resetPasswordRoute)
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody NewPasswordRequest request) {
        appUserService.updatePassword(token, request);
        return new ResponseEntity<>("Your password is changed", HttpStatus.OK);
    }

    @GetMapping(Routes.currentUserRoute)
    public ResponseEntity<?> getCurrentUser() {
        CurrentUserDto user = appUserService.getAuthenticatedUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
