package com.proxym.yacine.codintest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewInvitationRequest {

    private String candidateEmail;
    private Long technicalTestId;
    private String subject;
    private String content;
    private LocalDateTime expirationDate;
}
