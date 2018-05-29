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
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Timestamp;
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
@EqualsAndHashCode(exclude = {"eventTypeEvaluations", "students", "files", "managers", "organizations"})
@ToString(exclude = {"eventTypeEvaluations", "students", "files", "managers", "organizations"})
@Entity(name = "Event")
@Table(name = "event")
@Document(indexName = "event", type = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "event_seq_gen")
    @SequenceGenerator(name = "event_seq_gen", allocationSize = 1, sequenceName = "event_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(columnDefinition = "TEXT", name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private Timestamp timestamp;

    @Column(name = "event_date")
    private Timestamp eventDate;

    @Column(name = "event_type_name")
    private String eventTypeName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "event_to_type",
               joinColumns = @JoinColumn(name = "event_id"),
               inverseJoinColumns = @JoinColumn(name = "type_id"))
    private List<EventTypeEvaluation> eventTypeEvaluations;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_to_event",
               joinColumns = @JoinColumn(name = "stud_event_id"),
               inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "file_to_event",
            joinColumns = @JoinColumn(name = "event_file_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileOfService> files;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "event_manager",
               joinColumns = @JoinColumn(name = "manage_event_id"),
               inverseJoinColumns = @JoinColumn(name = "manager_id"))
    private Set<Profile> managers;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "event_to_organization",
            joinColumns = @JoinColumn(name = "event_agency_id"),
            inverseJoinColumns = @JoinColumn(name = "agency_id"))
    private List<Organization> organizations;

}
