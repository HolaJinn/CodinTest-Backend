package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.InvitationFilterOption;
import com.proxym.yacine.codintest.dto.request.NewInvitationRequest;
import com.proxym.yacine.codintest.dto.response.InvitationDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface InvitationService {

    Page<InvitationDto> findAll(InvitationFilterOption options);
    void create(NewInvitationRequest newInvitationRequest);
    void update(Long invitationId, Map<String, Object> changes);
    void delete(Long invitationId);
}
