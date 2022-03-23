package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.NewRecruiter;
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
@RequestMapping(Routes.ownerRoute)
public class OwnerController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/add-recruiter")
    public ResponseEntity<?> addRecruiter(@RequestBody NewRecruiter newRecruiter, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.saveRecruiter(newRecruiter, Utility.getSiteURL(request));
        return new ResponseEntity<>("New recruiter is added to your company", HttpStatus.CREATED);
    }
}
