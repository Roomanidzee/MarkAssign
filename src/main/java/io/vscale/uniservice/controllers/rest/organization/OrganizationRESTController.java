package io.vscale.uniservice.controllers.rest.organization;

import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.services.interfaces.events.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 19.03.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
@RestController
@RequestMapping("/api_v1/organization")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizationRESTController {

    private OrganizationService organizationService;

    @GetMapping("/show/organizations")
    @ResponseBody
    public List<Organization> getOrganizations(){
        return organizationService.findAll();
    }

    @GetMapping("/show/organizations/{id}")
    @ResponseBody
    public Organization getOrganizationById(@PathVariable("id") Long id){
        return organizationService.findById(id);
    }

    @DeleteMapping("/organization/delete/{id}")
    public void deleteOrganization(@PathVariable("id") Long id){
        organizationService.delete(id);
    }

    @PostMapping("/organization/save")
    public void saveOrganization(@RequestBody Organization organization){
        organizationService.save(organization);
    }


    @GetMapping("/show/headOfOrganization/organization")
    @ResponseBody
    public Set<Student> getOrganizationById(@RequestBody Organization organization){
        return organizationService.getHeadOfOrganization(organization);
    }

    @GetMapping("/show/organizations/asc")
    public ResponseEntity<Page<Organization>> getOrganizationsAsc(Pageable pageable){
        return ResponseEntity.ok(this.organizationService.retrieveSortedOrganizationsAsc(pageable));
    }

    @GetMapping("/show/organizations/desc")
    public ResponseEntity<Page<Organization>> getOrganizationsDesc(Pageable pageable){
        return ResponseEntity.ok(this.organizationService.retrieveSortedOrganizationsDesc(pageable));
    }

}
