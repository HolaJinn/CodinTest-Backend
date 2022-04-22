package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.NewRecruiter;
import com.proxym.yacine.codintest.dto.response.CurrentUserDto;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.CompanyService;
import com.proxym.yacine.codintest.util.Routes;
import com.proxym.yacine.codintest.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(Routes.companyRoute)
public class CompanyController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/add-recruiter")
    public ResponseEntity<?> addRecruiter(@RequestBody NewRecruiter newRecruiter, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        appUserService.saveRecruiter(newRecruiter, Utility.getSiteURL(request));
        return new ResponseEntity<>("New recruiter is added to your company", HttpStatus.CREATED);
    }

    @GetMapping("/recruiters")
    public Page<CurrentUserDto> getAllRecruiters(@RequestParam Map<String, Object> options) {
        return companyService.getAllRecruiters(options);
    }
}
