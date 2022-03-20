package com.proxym.yacine.codintest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "recruiters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Recruiter extends AppUser{
    @Column(name = "role_in_company")
    private String roleInCompany;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
