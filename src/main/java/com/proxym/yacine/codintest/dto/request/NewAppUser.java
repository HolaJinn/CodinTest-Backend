package com.proxym.yacine.codintest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NewAppUser {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
