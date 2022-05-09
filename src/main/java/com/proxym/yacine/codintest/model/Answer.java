package com.proxym.yacine.codintest.model;

import com.proxym.yacine.codintest.util.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Answer extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Exercise exercise;

    @OneToOne
    private AppUser user;

    private String code;
    private ExecutionResult executionResult;
    private Long score;

}
