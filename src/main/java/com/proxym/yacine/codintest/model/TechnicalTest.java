package com.proxym.yacine.codintest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "technical_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TechnicalTest extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private AppUser creator;

    private String title;
    private String description;

    @Column(name = "timer_in_minute")
    private Integer timerInMinute;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "technical_tests_exercises",
            joinColumns = @JoinColumn(name = "technical_test_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    )
    private Collection<Exercise> exercises = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
