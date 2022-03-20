package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.dto.request.NewRecruiter;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Company;
import com.proxym.yacine.codintest.model.Owner;
import com.proxym.yacine.codintest.model.Recruiter;
import com.proxym.yacine.codintest.repository.CompanyRepository;
import com.proxym.yacine.codintest.repository.OwnerRepository;
import com.proxym.yacine.codintest.repository.RecruiterRepository;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private AppUserService appUserService;


    /*
    * This method allows the owner to creat his company
    * He can have only on company at a time
    * */
    @Override
    public void createCompany(NewCompanyRequest newCompanyRequest) {
        AppUser appUser = appUserService.getCurrentAuthenticatedUser();
        Owner owner = ownerRepository.findById(appUser.getId())
                .orElseThrow(() -> new CustomException("No user found", "USER NOT FOUND", 404));

        if (!owner.isVerified()) {
            throw new CustomException("You should verify your email first","EMAIL NOT VERIFIED", 400);
        }
        if(owner.getCompany() != null) {
            throw new CustomException("You are a company owner already! You can't create more than one company",400);
        }
        owner.setRoleInCompany(newCompanyRequest.getRoleInCompany());
        Company company = Company.builder()
                .owner(owner)
                .name(newCompanyRequest.getName())
                .country(newCompanyRequest.getCountry())
                .isActive(true)
                .subscriptionDurationInDays(14)
                .build();
        companyRepository.save(company);
        owner.setCompany(company);
        ownerRepository.save(owner);
    }

    @Override
    public void createRecruiter(NewRecruiter newRecruiter, String siteURL) throws MessagingException, UnsupportedEncodingException {
        Company company = companyRepository.findById(newRecruiter.getCompany())
                .orElseThrow(() -> new CustomException("There is no company with such ID", "COMPANY NOT FOUND", 404));

        appUserService.saveAppUser(newRecruiter, siteURL);
        Recruiter recruiter = recruiterRepository.findByEmail(newRecruiter.getEmail());
        recruiter.setRoleInCompany(newRecruiter.getRoleInCompany());
        recruiter.setCompany(company);
        recruiterRepository.save(recruiter);
    }
}
