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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.SequenceGenerator;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;

import java.util.Date;

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
@EqualsAndHashCode(exclude = {"organization"})
@ToString(exclude = {"organization"})
@Entity(name = "Schedule")
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "schedule_seq_gen")
    @SequenceGenerator(name = "schedule_seq_gen", allocationSize = 1, sequenceName = "schedule_seq")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "day")
    private Date day;

    @Temporal(TemporalType.TIME)
    @Column(name = "time")
    private Date time;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
