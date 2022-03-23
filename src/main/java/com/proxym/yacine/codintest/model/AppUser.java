package com.proxym.yacine.codintest.model;

import com.proxym.yacine.codintest.util.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "app_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AppUser extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;

    @Column(name = "photo_url")
    private String photoURL;

    private UserState state;
    private boolean verified;

    @Column(name="verification_code", length = 64)
    private String verificationCode;

    @Column(name = "reset_password_token", length = 64)
    private String resetPasswordToken;

    @ManyToOne
    private Company company;

    private String roleInCompany;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany
    private List<Exercise> exercises;

}
