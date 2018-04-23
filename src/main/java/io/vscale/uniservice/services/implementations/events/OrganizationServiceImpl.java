package io.vscale.uniservice.services.implementations.events;

import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.repositories.data.OrganizationRepository;
import io.vscale.uniservice.services.interfaces.events.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 02.03.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
@Service
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
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
}
