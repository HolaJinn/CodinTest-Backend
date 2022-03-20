package com.proxym.yacine.codintest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "subscription_duration_in_days")
    private Integer subscriptionDurationInDays;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

}
