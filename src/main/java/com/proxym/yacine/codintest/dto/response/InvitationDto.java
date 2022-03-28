package com.proxym.yacine.codintest.dto.response;

import com.proxym.yacine.codintest.util.InvitationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvitationDto {

    private Long id;
    private Long invitedById;
    private String candidateEmail;
    private Long technicalTestId;
    private String subject;
    private LocalDateTime expirationDate;
    private InvitationState state;
    private Integer rating;
    private Long companyId;

}
