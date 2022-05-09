package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.InvitationFilterOption;
import com.proxym.yacine.codintest.dto.request.NewInvitationRequest;
import com.proxym.yacine.codintest.dto.response.CurrentUserDto;
import com.proxym.yacine.codintest.dto.response.InvitationDto;
import com.proxym.yacine.codintest.dto.response.RelatedCandidateResponse;
import com.proxym.yacine.codintest.model.Invitation;
import com.proxym.yacine.codintest.service.InvitationService;
import com.proxym.yacine.codintest.util.Routes;
import com.proxym.yacine.codintest.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(Routes.invitationsRoute)
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @GetMapping("")
    public Page<InvitationDto> findAll(@RequestParam Map<String, Object> options) {
        return invitationService.findAll(options);
    }

    @GetMapping("/my-invitations")
    public Page<InvitationDto> findCurrentUserInvitations(@RequestParam Map<String, Object> options) {
        return invitationService.findCurrentUserInvitations(options);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody NewInvitationRequest newInvitationRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        invitationService.create(newInvitationRequest, Utility.getSiteURL(request));
        return new ResponseEntity<>("New invitation is created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> changes) {
        invitationService.update(id, changes);
        return new ResponseEntity<>("Invitation is updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        invitationService.delete(id);
        return new ResponseEntity<>("Invitation is deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/accept-invitation/{id}")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        System.out.println("Hello controller");
        invitationService.acceptInvitation(id);
        return new ResponseEntity<>("Invitation accepted", HttpStatus.OK);
    }

    @PostMapping("/reject-invitation/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        invitationService.rejectInvitation(id);
        return new ResponseEntity<>("Invitation Rejected", HttpStatus.OK);
    }

    @GetMapping("/related-candidates")
    public Page<RelatedCandidateResponse> getAllRelatedCandidates(Pageable pageable) {
        return invitationService.getAllRelatedCandidates(pageable);
    }
}
