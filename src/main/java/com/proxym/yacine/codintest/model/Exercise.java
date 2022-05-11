package com.proxym.yacine.codintest.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proxym.yacine.codintest.util.ExerciseDifficulty;
import com.proxym.yacine.codintest.util.ExerciseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Exercise extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private AppUser creator;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private ExerciseDifficulty difficulty;
    private ExerciseStatus status;

    @Column(name = "timer_in_minute")
    private Integer timerInMinute;

    @OneToOne
    @JoinColumn(name = "programming_language_id")
    private ProgrammingLanguage programmingLanguage;

    @Column(name = "initial_code")
    private String initialCode;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TestCase> testCases;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "exercises_tags",
            joinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Collection<Tag> tags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
//
//    @ManyToOne
//    @JoinColumn(name = "technical_test_id")
//    private TechnicalTest technicalTest;
}
