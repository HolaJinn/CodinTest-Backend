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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Page<InvitationDto> findAll(Map<String, Object> options) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        BooleanBuilder builder = new BooleanBuilder();
        final QInvitation qInvitation = QInvitation.invitation;
        return doYourJob(qInvitation, builder, options, user);
    }

    private Page<InvitationDto> doYourJob(QInvitation qInvitation, BooleanBuilder builder, Map<String, Object> options, AppUser user) {
        int page = 0, limit = 10;

        if (options == null) {
            return invitationRepository.findAll(builder, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "CreatedDate", "id")))
                    .map(invitation -> modelMapper.map(invitation, InvitationDto.class));
        } else {

            page = (options.get("page") != null) ? Integer.parseInt((String) options.get("page")) : page;
            limit = (options.get("limit") != null) ? Integer.parseInt((String) options.get("limit")) : limit;

            if (options.get("state") != null) {
                if (!options.get("state").toString().equals("All")) {
                    builder.and(qInvitation.state.eq(InvitationState.valueOf((String) options.get("state"))));
                }
            };
            if (options.get("rated") != null) {
                if (options.get("rated").toString().equalsIgnoreCase("true")) {
                    builder.and(qInvitation.rating.ne(0));
                }
            }
            if(options.get("search") != null) {
                builder.andAnyOf(qInvitation.subject.containsIgnoreCase((String) options.get("search")),
                        qInvitation.candidateEmail.containsIgnoreCase((String) options.get("search"))
                );
            }
            if(options.get("createdByMe") != null){
                if(options.get("createdByMe").toString().equals("true")) {
                    builder.and(qInvitation.invitedBy.id.eq(user.getId()));
                }
            }

            Sort.Direction direction = (options.get("order").equals(Order.DESC)) ? Sort.Direction.DESC : Sort.Direction.ASC;
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, ((String) options.get("properties"))));
            return (builder.getValue() != null)
                    ? invitationRepository.findAll(builder, pageRequest).map(invitation -> modelMapper.map(invitation, InvitationDto.class))
                    : invitationRepository.findAll(pageRequest).map(invitation -> modelMapper.map(invitation, InvitationDto.class));
        }
    }

    @Override
    public Invitation getById(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new CustomException("There is no invitation with such ID", "INVITATION NOT FOUND", 404));
        return invitation;
    }

    @Override
    public Page<InvitationDto> findCurrentUserInvitations(Map<String, Object> options) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        BooleanBuilder builder = new BooleanBuilder();
        final QInvitation qInvitation = QInvitation.invitation;
        return findMyInvitations(qInvitation, builder, options, user);
    }

    private Page<InvitationDto> findMyInvitations(QInvitation qInvitation, BooleanBuilder builder, Map<String, Object> options, AppUser user) {
        int page = 0, limit = 10;

        builder.and(qInvitation.candidateEmail.equalsIgnoreCase(user.getEmail()));

        if (options == null) {
            return invitationRepository.findAll(builder, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "CreatedDate", "id")))
                    .map(invitation -> modelMapper.map(invitation, InvitationDto.class));
        } else {

            page = (options.get("page") != null) ? Integer.parseInt((String) options.get("page")) : page;
            limit = (options.get("limit") != null) ? Integer.parseInt((String) options.get("limit")) : limit;

            if (options.get("state") != null) builder.and(qInvitation.state.eq(InvitationState.valueOf((String) options.get("state"))));
            if (options.get("rated") != null) {
                if (options.get("rated").toString().equalsIgnoreCase("true")) {
                    builder.and(qInvitation.rating.ne(0));
                }
            }
            if(options.get("search") != null) {
                    builder.andAnyOf(qInvitation.subject.containsIgnoreCase((String) options.get("search")),
                            qInvitation.invitedBy.firstName.containsIgnoreCase((String) options.get("search")),
                            qInvitation.invitedBy.lastName.containsIgnoreCase((String) options.get("search")),
                            qInvitation.company.name.containsIgnoreCase((String) options.get("search"))
                    );
            }

            Sort.Direction direction = (options.get("order").equals(Order.DESC.name())) ? Sort.Direction.DESC : Sort.Direction.ASC;
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, ((String) options.get("properties"))));
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
                .state(InvitationState.Pending)
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
            throw new CustomException("You are not allowed to delete this invitation", "UNAUTHORIZED", 403);
        }
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new CustomException("There is no invitation with such ID", "INVITATION NOT FOUND", 404));
        invitationRepository.delete(invitation);
        log.info(String.format("Invitation with ID %s is deleted successfully", invitationId));
    }

    @Override
    public void checkForExpiration() {
        List<Invitation> invitations = invitationRepository.findInvitationByState(InvitationState.Pending);
        LocalDateTime now = LocalDateTime.now();
        invitations.stream().map((invitation -> {
            if (now.compareTo(invitation.getExpirationDate()) > 0) {
                invitation.setState(InvitationState.Expired);
                invitationRepository.save(invitation);
            }
            return null;
        })).collect(Collectors.toList());
    }

    @Override
    public void acceptInvitation(Long invitationId) {
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        Invitation invitation = getById(invitationId);
        System.out.println(user.getEmail());
        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }
        System.out.println("test here");
        if(!user.getEmail().equals(invitation.getCandidateEmail())) {
            throw new CustomException("You are not the original receiver of this invitation", "UNAUTHORIZED", 403);
        }

        if(!invitation.getState().equals(InvitationState.Pending)) {
            throw new CustomException("This invitation has expired already you can't accept it anymore!", "BAD REQUEST", 400);
        }
        if(invitation.getState().equals(InvitationState.Accepted)) {
            throw new CustomException("You already accepted this invitation", "BAD REQUEST", 400);
        }
        invitation.setState(InvitationState.Accepted);
        invitationRepository.save(invitation);
        log.info(String.format("Invitation with ID %s is accepted", invitationId));
    }

    @Override
    public void rejectInvitation(Long invitationId) {
        Invitation invitation = getById(invitationId);
        AppUser user = appUserService.getCurrentAuthenticatedUser();
        if (!user.isVerified()) {
            throw new CustomException("You should verify your account first","NOT VERIFIED", 400);
        }
        if(!user.getEmail().equals(invitation.getCandidateEmail())) {
            throw new CustomException("You are not the original receiver of this invitation", "UNAUTHORIZED", 403);
        }
        if(invitation.getState().equals(InvitationState.Rejected)) {
            throw new CustomException("You already rejected this invitation", "BAD REQUEST", 400);
        }

        if (invitation.getState().equals(InvitationState.Expired)) {
            throw new CustomException("This invitation had expired already", "BAD REQUEST", 400);
        }
        invitation.setState(InvitationState.Rejected);
        invitationRepository.save(invitation);
        log.info(String.format("Invitation with ID %s is reject", invitationId));
    }
}
