package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewInvitationRequest;
import com.proxym.yacine.codintest.dto.response.InvitationDto;
import com.proxym.yacine.codintest.model.Invitation;
import org.springframework.data.domain.Page;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface InvitationService {

    Page<InvitationDto> findAll(Map<String, Object> options);
    Page<InvitationDto> findCurrentUserInvitations(Map<String, Object> options);
    Invitation getById(Long id);
    void create(NewInvitationRequest newInvitationRequest,String siteURL) throws MessagingException, UnsupportedEncodingException;
    void sendInvitationLink(NewInvitationRequest newInvitationRequest,String token, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void update(Long invitationId, Map<String, Object> changes);
    void delete(Long invitationId);
    void checkForExpiration();
    void acceptInvitation(Long invitationId);
    void rejectInvitation(Long invitationId);
}
