package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.dto.request.NewRecruiter;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface OwnerService {

    void createCompany(NewCompanyRequest newCompanyRequest);
    void createRecruiter(NewRecruiter newRecruiter, String siteURL) throws MessagingException, UnsupportedEncodingException;
}
