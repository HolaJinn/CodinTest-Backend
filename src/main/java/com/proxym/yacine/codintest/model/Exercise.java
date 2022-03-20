package com.proxym.yacine.codintest.model;


import com.proxym.yacine.codintest.util.ExerciseDifficulty;
import com.proxym.yacine.codintest.util.ExerciseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private ExerciseDifficulty difficulty;
    private ExerciseStatus status;

    @Column(name = "timer_in_minute")
    private Integer timerInMinute;

    @ManyToOne
    @JoinColumn(name = "programming_language_id")
    private ProgrammingLanguage programmingLanguage;

    @Column(name = "initial_code")
    private String initialCode;

    @OneToMany
    private List<TestCase> testCases;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "exercises_tags",
            joinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Tag> tags = new ArrayList<>();


    @ManyToOne
    private AppUser appUser;
}
