package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.services.interfaces.events.OrganizationService;
import io.vscale.uniservice.utils.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

/**
 * 26.03.2018
 *
 * @author Andrey Romanov and Dias Arkharov
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/organizations")
    public ModelAndView showOrganizations(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Organization> pageWrapper =
                new PageWrapper<>(this.organizationService.findAll(pageable), "/admin/organizations");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;

    }

    @GetMapping("/organizations/asc")
    public ModelAndView showOrganizationsAsc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Organization> pageWrapper =
                    new PageWrapper<>(this.organizationService.retrieveSortedOrganizationsAsc(pageable),
                                                                    "/admin/organizations/asc");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;
    }

    @GetMapping("/organizations/desc")
    public ModelAndView showOrganizationsDesc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Organization> pageWrapper =
                new PageWrapper<>(this.organizationService.retrieveSortedOrganizationsDesc(pageable),
                                                                       "/admin/organizations/desc");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;
    }

    @GetMapping("/organizations/show/{id}")
    public ModelAndView showOrganization(@PathVariable("id") Long id){

        Organization organization = this.organizationService.findById(id);
        Set<Event> events = organization.getEvents();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("events", events);
        modelAndView.setViewName("organizations/view-organization");

        return new ModelAndView("organizations/view-organization");

    }

    @GetMapping("/organizations/edit/id")
    public ModelAndView editOrganization(){
        return new ModelAndView("organizations/edit-organization");
    }

    @PostMapping("/organizations/search")
    public ModelAndView searchOrganization(@RequestParam("search") String searchQuery){

        PageWrapper<Organization> pageWrapper =
                new PageWrapper<>(this.organizationService.searchByTitle(searchQuery), "/admin/organizations/search");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;

    }

}
