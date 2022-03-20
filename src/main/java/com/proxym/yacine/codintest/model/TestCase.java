package com.proxym.yacine.codintest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "test_case")
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String input;
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "execise_id")
    private Exercise exercise;
}
