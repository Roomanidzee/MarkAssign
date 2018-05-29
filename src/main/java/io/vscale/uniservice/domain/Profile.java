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
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.SequenceGenerator;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

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
@EqualsAndHashCode(exclude = {"user", "cooperator", "student", "confirmations", "manageEvents", "avatar"})
@ToString(exclude = {"user", "cooperator", "student", "confirmations", "manageEvents", "avatar"})
@Entity(name = "Profile")
@Table(name = "profile")
@Document(indexName = "profile", type = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "profile_seq_gen")
    @SequenceGenerator(name = "profile_seq_gen", allocationSize = 1, sequenceName = "profile_seq")
    private Long id;

    @Column(name = "surname", columnDefinition = "VARCHAR(30)")
    private String surname;

    @Column(name = "name", columnDefinition = "VARCHAR(30)")
    private String name;

    @Column(name = "patronymic", columnDefinition = "VARCHAR(50)")
    private String patronymic;

    @Column(name = "email", columnDefinition = "VARCHAR(60)")
    private String email;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cooperator cooperator;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Student student;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Confirmation> confirmations;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "event_manager",
               joinColumns = @JoinColumn(name = "manager_id"),
               inverseJoinColumns = @JoinColumn(name = "manage_event_id"))
    private Set<Event> manageEvents;

    @OneToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "avatar_id")
    private FileOfService avatar;

}
