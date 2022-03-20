package com.proxym.yacine.codintest.model;

import com.proxym.yacine.codintest.util.CandidateState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Candidate extends AppUser{

    @Column(name = "candidate_state")
    private CandidateState candidateState;
}
