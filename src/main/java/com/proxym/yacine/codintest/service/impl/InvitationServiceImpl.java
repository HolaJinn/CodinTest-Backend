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
import com.proxym.yacine.codintest.util.Constants;
import com.proxym.yacine.codintest.util.InvitationState;
import com.proxym.yacine.codintest.util.Order;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
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
    private JavaMailSender mailSender;

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
    public void create(NewInvitationRequest newInvitationRequest, String siteURL) throws MessagingException, UnsupportedEncodingException {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }

        if (user.getRole().getId().intValue() == 4){
            throw new CustomException("You are not allowed to create technical tests", "UNAUTHORIZED", 403);
        }

        TechnicalTest technicalTest = technicalTestRepository.findById(newInvitationRequest.getTechnicalTestId())
                .orElseThrow(() -> new CustomException("There is no technical test with such ID", "TECHNICAL TEST NOT FOUND", 404));

        String randomToken = RandomString.make(64);
        Invitation invitation = Invitation.builder()
                .invitedBy(user)
                .candidateEmail(newInvitationRequest.getCandidateEmail())
                .technicalTest(technicalTest)
                .subject(newInvitationRequest.getSubject())
                .expirationDate(newInvitationRequest.getExpirationDate())
                .state(InvitationState.PENDING)
                .verificationToken(randomToken)
                .company(user.getCompany())
                .build();

        invitationRepository.save(invitation);
        log.info(String.format("New invitation is created"));
        sendInvitationLink(newInvitationRequest, randomToken, siteURL);
    }

    @Override
    public void sendInvitationLink(NewInvitationRequest newInvitationRequest, String token, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = newInvitationRequest.getCandidateEmail();
        String fromAddress = "benamoryacine98@gmail.com";
        String senderName = "CodinTest";
        String subject = newInvitationRequest.getSubject();
        String content = "Dear Candidate,<br>"
                + "Please click the link below to access your technical test assignment:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Test Link</a></h3>"
                + newInvitationRequest.getContent() + "<br>"
                + "Thank you,<br>"
                + "CodinTest.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String invitationURL = Constants.CLIENT_URL + "candidate/invitation?token=" + token;
        content = content.replace("[[URL]]", invitationURL);

        helper.setText(content, true);
        mailSender.send(message);
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
