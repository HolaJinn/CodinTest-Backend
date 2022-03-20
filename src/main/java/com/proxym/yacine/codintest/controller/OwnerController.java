package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.dto.request.NewRecruiter;
import com.proxym.yacine.codintest.service.OwnerService;
import com.proxym.yacine.codintest.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @PostMapping("/create-company")
    public ResponseEntity<String> createCompany(@RequestBody NewCompanyRequest newCompanyRequest) {
        ownerService.createCompany(newCompanyRequest);
        return new ResponseEntity<>("Your company's virtual space is created", HttpStatus.CREATED);
    }

    @PostMapping("/create-recruiter")
    public ResponseEntity<String> createRecruiter(@RequestBody NewRecruiter newRecruiter, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        ownerService.createRecruiter(newRecruiter, Utility.getSiteURL(request));
        return new ResponseEntity<>("New recruiter is added to your company's virtual space", HttpStatus.CREATED);
    }
}
