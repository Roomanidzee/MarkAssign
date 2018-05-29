package io.vscale.uniservice.domain;

import io.vscale.uniservice.security.states.EducationState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.SequenceGenerator;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.util.List;
import java.util.Set;

/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"profile", "groups", "events", "confirmations", "organizations", "headOrganizations", "evaluations"})
@ToString(exclude = {"profile", "groups", "events", "confirmations", "organizations", "headOrganizations", "evaluations"})
@Entity(name = "Student")
@Table(name = "student")
@Document(indexName = "student", type = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "student_seq_gen")
    @SequenceGenerator(name = "student_seq_gen", allocationSize = 1, sequenceName = "student_seq")
    private Long id;

    @Column(name = "course")
    private Byte course;

    @Enumerated(EnumType.STRING)
    @Column(name = "education_type")
    private EducationState educationType;

    @Column(name = "gender", columnDefinition = "VARCHAR(7)")
    private String gender;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_to_group",
               joinColumns = @JoinColumn(name = "student_id"),
               inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> groups;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "student_to_event",
               joinColumns = @JoinColumn(name = "student_id"),
               inverseJoinColumns = @JoinColumn(name = "stud_event_id"))
    private List<Event> events;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_to_confirmation",
               joinColumns = @JoinColumn(name = "confirm_student_id"),
               inverseJoinColumns = @JoinColumn(name = "confirmation_id"))
    private Set<Confirmation> confirmations;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_to_organization",
               joinColumns = @JoinColumn(name = "part_student_id"),
               inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Set<Organization> organizations;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "head_of_organization",
               joinColumns = @JoinColumn(name = "head_student_id"),
               inverseJoinColumns = @JoinColumn(name = "head_organization_id"))
    private Set<Organization> headOrganizations;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "evaluation_to_student",
               joinColumns = @JoinColumn(name = "eval_student_id"),
               inverseJoinColumns = @JoinColumn(name = "stud_evaluation_id"))
    private Set<EventTypeEvaluation> evaluations;

}
