package io.vscale.uniservice.domain;

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

import java.util.Set;

/**
 * 02.03.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"schedule", "students", "headStudents", "organizationFiles", "events"})
@ToString(exclude = {"schedule", "students", "headStudents", "organizationFiles", "events"})
@Entity(name = "Organization")
@Table(name = "organization")
@Document(indexName = "organization", type = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "organization_seq_gen")
    @SequenceGenerator(name = "organization_seq_gen", allocationSize = 1, sequenceName = "organization_seq")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "organization", fetch = FetchType.LAZY)
    private Schedule schedule;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_to_organization",
               joinColumns = @JoinColumn(name = "organization_id"),
               inverseJoinColumns = @JoinColumn(name = "part_student_id"))
    private Set<Student> students;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "head_of_organization",
               joinColumns = @JoinColumn(name = "head_organization_id"),
               inverseJoinColumns = @JoinColumn(name = "head_student_id"))
    private Set<Student> headStudents;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "file_to_organization",
               joinColumns = @JoinColumn(name = "file_organization_id"),
               inverseJoinColumns = @JoinColumn(name = "org_file_id"))
    private Set<FileOfService> organizationFiles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "event_to_organization",
               joinColumns = @JoinColumn(name = "agency_id"),
               inverseJoinColumns = @JoinColumn(name = "event_agency_id"))
    private Set<Event> events;

}
