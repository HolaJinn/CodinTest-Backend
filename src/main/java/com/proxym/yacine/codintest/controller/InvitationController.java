package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.InvitationFilterOption;
import com.proxym.yacine.codintest.dto.request.NewInvitationRequest;
import com.proxym.yacine.codintest.dto.response.InvitationDto;
import com.proxym.yacine.codintest.service.InvitationService;
import com.proxym.yacine.codintest.util.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(Routes.invitationsRoute)
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @GetMapping("")
    public Page<InvitationDto> findAll(@RequestBody InvitationFilterOption options) {
        return invitationService.findAll(options);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody NewInvitationRequest newInvitationRequest) {
        invitationService.create(newInvitationRequest);
        return new ResponseEntity<>("New invitation is created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> changes) {
        invitationService.update(id, changes);
        return new ResponseEntity<>("Invitation is updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        System.out.println("Hello controller");
        invitationService.delete(id);
        return new ResponseEntity<>("Invitation is deleted successfully", HttpStatus.OK);
    }
}
