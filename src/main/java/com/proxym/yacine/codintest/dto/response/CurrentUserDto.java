package com.proxym.yacine.codintest.dto.response;

import com.proxym.yacine.codintest.model.Role;
import com.proxym.yacine.codintest.util.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentUserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private UserState state;
    private Boolean verified;
    private Role role;
}
