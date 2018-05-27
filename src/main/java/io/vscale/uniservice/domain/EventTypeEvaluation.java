package io.vscale.uniservice.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import java.util.List;
import java.util.Set;

/**
 * 01.03.2018
 *
 * @author Aynur Aymurzin
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"events"})
@ToString(exclude = {"events"})
@Entity(name = "EventTypeEvaluation")
@Table(name = "event_type_evaluation")
public class EventTypeEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "event_type_seq_gen")
    @SequenceGenerator(name = "event_type_seq_gen", allocationSize = 1, sequenceName = "event_type_seq")
    private Long id;

    @Column(columnDefinition = "TEXT", name = "type", nullable = false)
    private String type;

    @Column(name = "start_value", nullable = false)
    private Byte startValue;

    @Column(name = "end_value", nullable = false)
    private Byte endValue;

    @Column(name = "final_value")
    private Byte finalValue;

    @Column(name = "student_role")
    private String studentRole;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "eventTypeEvaluations")
    private List<Event> events;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "evaluation_to_student",
               joinColumns = @JoinColumn(name = "stud_evaluation_id"),
               inverseJoinColumns = @JoinColumn(name = "eval_student_id"))
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "subjects_to_evaluation",
               joinColumns = @JoinColumn(name = "evaluation_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<SubjectsToCourse> subjects;

}
