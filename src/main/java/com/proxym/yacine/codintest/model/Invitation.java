package com.proxym.yacine.codintest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proxym.yacine.codintest.util.InvitationState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invitations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Invitation extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private AppUser invitedBy;

    @OneToOne
    private TechnicalTest technicalTest;

    @Column(name = "candidate_email", nullable = false)
    private String candidateEmail;

    private String subject;

    @Column(name = "expirtation_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;

    private InvitationState state;

    private Integer rating;

    @ManyToOne
    private Company company;

}
