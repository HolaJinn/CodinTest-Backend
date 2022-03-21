package com.proxym.yacine.codintest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@Entity
@Table(name = "test_case")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TestCase extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long score;

    @Column(name = "is_sample")
    private boolean isSample;
    private String input;

    @Column(name = "expected_output")
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "execise_id")
    private Exercise exercise;
}
