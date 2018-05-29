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
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
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
@EqualsAndHashCode(exclude = {"confirmations", "organizations", "cooperators", "events", "profile"})
@ToString(exclude = {"confirmations", "organizations", "cooperators", "events", "profile"})
@Entity(name = "FileOfService")
@Table(name = "files_of_service")
@Document(indexName = "file", type = "files")
public class FileOfService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "file_seq_gen")
    @SequenceGenerator(name = "file_seq_gen", allocationSize = 1, sequenceName = "file_seq")
    private Long id;

    @Column(columnDefinition = "TEXT", name = "original_name", nullable = false)
    private String originalName;

    @Column(columnDefinition = "TEXT", name = "encoded_name", nullable = false)
    private String encodedName;

    @Column(length = 50, name = "type", nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT", name = "url", nullable = false)
    private String url;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "files")
    private Set<Event> events;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "group_statistics",
               joinColumns = @JoinColumn(name = "coop_file_id"),
               inverseJoinColumns = @JoinColumn(name = "cooperator_id"))
    private Set<Cooperator> cooperators;

    @OneToMany(mappedBy = "fileOfService", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Confirmation> confirmations;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "file_to_organization",
               joinColumns = @JoinColumn(name = "org_file_id"),
               inverseJoinColumns = @JoinColumn(name = "file_organization_id"))
    private Set<Organization> organizations;

    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Profile profile;

}
