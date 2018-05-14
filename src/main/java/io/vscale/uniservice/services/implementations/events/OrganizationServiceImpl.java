package io.vscale.uniservice.services.implementations.events;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.repositories.data.OrganizationRepository;
import io.vscale.uniservice.repositories.indexing.OrganizationESRepository;
import io.vscale.uniservice.services.interfaces.events.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 02.03.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
@Service
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;
    private final OrganizationESRepository organizationESRepository;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   OrganizationESRepository organizationESRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationESRepository = organizationESRepository;
    }

    @Override
    public Set<Student> getHeadOfOrganization(Organization organization) {
        return this.organizationRepository.findOne(organization.getId()).getHeadStudents();
    }

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization findById(Long id) {
        return organizationRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        organizationRepository.delete(id);
    }

    @Override
    public void delete(Organization organization) {
        organizationRepository.delete(organization);
    }

    @Override
    public void save(Organization organization) {
        organizationRepository.save(organization);
    }

    @Override
    public Page<Organization> findAll(Pageable pageable) {
        return this.organizationRepository.findAll(pageable);
    }

    @Override
    public Page<Organization> retrieveSortedOrganizationsAsc(Pageable pageable) {

        Long number;

        if(pageable.getPageNumber() == 1){
            number = (long)0;
        }else{
            number = (long) (pageable.getPageNumber() + 3);
        }

        List<Organization> organizations = this.organizationRepository.findAllByOrderByTitleAsc(number);

        return new PageImpl<>(organizations, pageable, organizations.size());

    }

    @Override
    public Page<Organization> retrieveSortedOrganizationsDesc(Pageable pageable) {

        Long number;

        if(pageable.getPageNumber() == 1){
            number = (long)0;
        }else{
            number = (long) (pageable.getPageNumber() + 3);
        }

        List<Organization> organizations = this.organizationRepository.findAllByOrderByTitleDesc(number);

        return new PageImpl<>(organizations, pageable, organizations.size());

    }

    @Override
    public Set<FileOfService> getEventFiles(Long organizationId) {
        return this.organizationRepository.findOne(organizationId).getOrganizationFiles();
    }

    @Override
    public Map<Event, Set<FileOfService>> getEventsWithFile(Long organizationId) {

        Organization organization = this.organizationRepository.findOne(organizationId);
        Set<Event> events = organization.getEvents();

        return events.stream()
                     .collect(Collectors.toMap(event -> event, Event::getFiles, (a, b) -> b));

    }

    @Override
    public Integer getNumberOfPeople(Long id) {
        return organizationRepository.findOne(id).getStudents().size();
    }

    @Override
    public Set<Student> getParticipants(Long id) {
        return organizationRepository.findOne(id).getStudents();
    }

    @Override
    public Page<Organization> searchByTitle(String title) {

        List<Organization> organizations = this.organizationESRepository.findByTitle(title);

        return new PageImpl<>(organizations, null, organizations.size());
    }

    @Override
    public Long getOrganizationsCount() {
        return this.organizationRepository.count();
    }
}
