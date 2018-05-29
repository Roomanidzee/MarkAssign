package io.vscale.uniservice.services.implementations.events;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.forms.general.OrganizationForm;
import io.vscale.uniservice.repositories.data.OrganizationRepository;
import io.vscale.uniservice.repositories.indexing.OrganizationESRepository;
import io.vscale.uniservice.services.interfaces.events.OrganizationService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final StudentService studentService;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   OrganizationESRepository organizationESRepository,
                                   StudentService studentService) {
        this.organizationRepository = organizationRepository;
        this.organizationESRepository = organizationESRepository;
        this.studentService = studentService;
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
    public Page<Organization> searchByTitle(String title, Pageable pageable) {

        List<Organization> organizations = this.organizationESRepository.findByTitle(title);

        return new PageImpl<>(organizations, pageable, organizations.size());
    }

    @Override
    public Long getOrganizationsCount() {
        return this.organizationRepository.count();
    }

    @Override
    public List<String> getTypesOfOrganizations() {
        return this.organizationRepository.findAll()
                                          .stream()
                                          .map(Organization::getType)
                                          .distinct()
                                          .collect(Collectors.toList());
    }

    @Override
    public void addOrganization(String title, String type) {

        Organization organization = Organization.builder()
                                                .title(title)
                                                .type(type)
                                                .build();

        this.organizationRepository.save(organization);
        this.organizationESRepository.save(organization);

    }

    @Override
    public void updateOrganization(OrganizationForm organizationForm) {

        Organization organization = this.organizationRepository.findOne(organizationForm.getId());
        Student student = this.studentService.getStudentById(organizationForm.getDirectorId());

        if(organization.getHeadStudents() == null){
            organization.setHeadStudents(Collections.singleton(student));
        }else{
            organization.getHeadStudents().add(student);
        }

        organization.setTitle(organizationForm.getTitle());
        organization.setType(organizationForm.getType());
        organization.setDescription(organizationForm.getDescription());

        this.organizationRepository.save(organization);
        this.organizationESRepository.save(organization);

    }

    @Override
    public void deleteEvent(Long organizationId, Long eventId) {

        Organization organization = this.organizationRepository.findOne(organizationId);
        organization.getEvents().removeIf(event -> event.getId().equals(eventId));
        this.organizationRepository.save(organization);

    }

    @Override
    public void deleteParticipant(Long organizationId, Long participantId) {

        Organization organization = this.organizationRepository.findOne(organizationId);
        organization.getStudents().removeIf(student -> student.getId().equals(participantId));
        this.organizationRepository.save(organization);

    }

    @Override
    public Set<Event> getEvents(Long organisationId) {

        Organization organization = this.organizationRepository.findOne(organisationId);

        return organization.getEvents();
    }
}
