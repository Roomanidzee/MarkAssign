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
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.SequenceGenerator;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
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
@EqualsAndHashCode(exclude = {"students"})
@ToString(exclude = {"students"})
@Entity(name = "Group")
@Table(name = "group_of_faculty")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "group_seq_gen")
    @SequenceGenerator(name = "group_seq_gen", allocationSize = 1, sequenceName = "group_seq")
    private Long id;

    @Column(name = "title", columnDefinition = "VARCHAR(15)")
    private String title;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_to_group",
               joinColumns = @JoinColumn(name = "group_id"),
               inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students;

}
