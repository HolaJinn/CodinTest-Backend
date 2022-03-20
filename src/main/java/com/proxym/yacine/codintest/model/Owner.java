package com.proxym.yacine.codintest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "owners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Owner extends AppUser{

    @Column(name = "role_in_company")
    private String roleInCompany;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
