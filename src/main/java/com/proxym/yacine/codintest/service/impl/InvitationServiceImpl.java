package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.InvitationFilterOption;
import com.proxym.yacine.codintest.dto.request.NewInvitationRequest;
import com.proxym.yacine.codintest.dto.response.InvitationDto;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.AppUser;
import com.proxym.yacine.codintest.model.Invitation;
import com.proxym.yacine.codintest.model.QInvitation;
import com.proxym.yacine.codintest.model.TechnicalTest;
import com.proxym.yacine.codintest.repository.InvitationRepository;
import com.proxym.yacine.codintest.repository.TechnicalTestRepository;
import com.proxym.yacine.codintest.service.AppUserService;
import com.proxym.yacine.codintest.service.InvitationService;
import com.proxym.yacine.codintest.util.InvitationState;
import com.proxym.yacine.codintest.util.Order;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private TechnicalTestRepository technicalTestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<InvitationDto> findAll(InvitationFilterOption options) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        BooleanBuilder builder = new BooleanBuilder();
        final QInvitation qInvitation = QInvitation.invitation;
        return doYourJob(qInvitation, builder, options, user);
    }

    private Page<InvitationDto> doYourJob(QInvitation qInvitation, BooleanBuilder builder, InvitationFilterOption options, AppUser user) {
        int page = 0, limit = 10;

        if (options == null) {
            return invitationRepository.findAll(builder, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "CreatedDate", "id")))
                    .map(invitation -> modelMapper.map(invitation, InvitationDto.class));
        } else {
            page = (options.getPage() != null) ? options.getPage() : page;
            limit = (options.getLimit() != null) ? options.getLimit() : limit;

            if (options.getState() != null) builder.and(qInvitation.state.eq(options.getState()));
            if (options.getIsRated() != null) {
                if (options.getIsRated().booleanValue()) {
                    builder.and(qInvitation.rating.ne(0));
                }
            }
            Sort.Direction direction = (options.getOrder().equals(Order.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, options.getProperties()));
            return (builder.getValue() != null)
                    ? invitationRepository.findAll(builder, pageRequest).map(invitation -> modelMapper.map(invitation, InvitationDto.class))
                    : invitationRepository.findAll(pageRequest).map(invitation -> modelMapper.map(invitation, InvitationDto.class));
        }
    }

    @Override
    public void create(NewInvitationRequest newInvitationRequest) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }

        if (user.getRole().getId().intValue() == 4){
            throw new CustomException("You are not allowed to create technical tests", "UNAUTHORIZED", 403);
        }

        TechnicalTest technicalTest = technicalTestRepository.findById(newInvitationRequest.getTechnicalTestId())
                .orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));

        Invitation invitation = Invitation.builder()
                .invitedBy(user)
                .candidateEmail(newInvitationRequest.getCandidateEmail())
                .technicalTest(technicalTest)
                .subject(newInvitationRequest.getSubject())
                .expirationDate(newInvitationRequest.getExpirationDate())
                .state(InvitationState.PENDING)
                .company(user.getCompany())
                .build();

        invitationRepository.save(invitation);
        log.info(String.format("New invitation is created"));
    }

    @Override
    public void update(Long invitationId, Map<String, Object> changes) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }

        if (user.getRole().getId().intValue() == 4){
            throw new CustomException("You are not allowed to create technical tests", "UNAUTHORIZED", 403);
        }

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new CustomException("There is no invitation with such ID", "TECHNICAL TEST NOT FOUND", 404));
        try {
            changes.forEach((key, value) -> {
                switch (key) {
                    case "subject" : {
                        invitation.setSubject((String) value);
                        break;
                    }
                    case "expirationDate": {
                        invitation.setExpirationDate((LocalDateTime) value);
                        break;
                    }
                    case "state": {
                        invitation.setState((InvitationState) value);
                        break;
                    }
                    case "rating": {
                        invitation.setRating((Integer) value);
                        break;
                    }
                }
            });
            invitationRepository.save(invitation);
            log.info(String.format("Invitation with ID %s is updated successfully", invitationId));
        } catch(Exception e) {
            throw new CustomException("Something went wrong", e.getMessage(), 400);
        }
    }

    @Override
    public void delete(Long invitationId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }

        if (user.getRole().getId().intValue() == 4){
            throw new CustomException("You are not allowed to create technical tests", "UNAUTHORIZED", 403);
        }
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new CustomException("There is no invitation with such ID", "TECHNICAL TEST NOT FOUND", 404));
        invitationRepository.delete(invitation);
        log.info(String.format("Invitation with ID %s is deleted successfully", invitationId));
    }
}
