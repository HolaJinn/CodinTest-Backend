package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewCompanyRequest;
import com.proxym.yacine.codintest.dto.response.RecruiterDto;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Company;
import com.proxym.yacine.codintest.model.QAppUser;
import com.proxym.yacine.codintest.repository.AppUserRepository;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.CompanyService;
import com.proxym.yacine.codintest.util.Order;
import com.querydsl.core.BooleanBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Company save(NewCompanyRequest newCompanyRequest) {
        return null;
    }

    @Override
    public Page<RecruiterDto> getAllRecruiters(Map<String, Object> options) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        BooleanBuilder builder = new BooleanBuilder();
        final QAppUser qAppUser = QAppUser.appUser;
        return doYourJob(qAppUser, builder, options, user);
    }

    private Page<RecruiterDto> doYourJob(QAppUser qAppUser, BooleanBuilder builder, Map<String, Object> options, AppUser user) {
        int page = 0, limit = 10;
        builder.and(qAppUser.company.id.eq(user.getCompany().getId()));
        if(options == null) {
            return appUserRepository.findAll(builder, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "CreatedDate", "id")))
                    .map(appUser -> modelMapper.map(appUser, RecruiterDto.class));
        } else {
            page = (options.get("page") != null) ? Integer.parseInt((String) options.get("page")) : page;
            limit = (options.get("limit") != null) ? Integer.parseInt((String) options.get("limit")) : limit;

            if(options.get("search") != null) {
                builder.andAnyOf(
                        qAppUser.firstName.containsIgnoreCase((String) options.get("search")),
                        qAppUser.lastName.containsIgnoreCase((String) options.get("search")),
                        qAppUser.email.containsIgnoreCase((String) options.get("search"))
                        );
            }

            Sort.Direction direction = (options.get("order").equals(Order.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, ((String) options.get("properties"))));
            return (builder.getValue() != null)
                    ? appUserRepository.findAll(builder, pageRequest).map(appUser -> modelMapper.map(appUser, RecruiterDto.class))
                    : appUserRepository.findAll(pageRequest).map(appUser -> modelMapper.map(appUser, RecruiterDto.class));
        }
    }
}
