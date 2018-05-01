package io.vscale.uniservice.services.interfaces.events;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 02.03.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
public interface OrganizationService {

    Set<Student> getHeadOfOrganization(Organization organization);

    List<Organization> findAll();

    Organization findById(Long id);

    void delete(Long id);

    void delete(Organization organization);

    void save(Organization organization);

    Page<Organization> findAll(Pageable pageable);

    Page<Organization> retrieveSortedOrganizationsAsc(Pageable pageable);
    Page<Organization> retrieveSortedOrganizationsDesc(Pageable pageable);

    Set<FileOfService> getEventFiles(Long organizationId);
    Map<Event, Set<FileOfService>> getEventsWithFile(Long organizationId);

    Integer getNumberOfPeople(Long id);

    Set<Student> getParticipants(Long id);

}
