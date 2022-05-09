package com.proxym.yacine.codintest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatedCandidateResponse {

    private String email;
    private String firstName;
    private String lastName;
}
