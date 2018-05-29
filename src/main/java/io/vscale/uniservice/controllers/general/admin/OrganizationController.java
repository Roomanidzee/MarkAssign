package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.forms.general.OrganizationForm;
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
        Long limit = this.organizationService.getOrganizationsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @GetMapping("/organizations/asc")
    public ModelAndView showOrganizationsAsc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Organization> pageWrapper =
                    new PageWrapper<>(this.organizationService.retrieveSortedOrganizationsAsc(pageable),
                                                                    "/admin/organizations/asc");
        Long limit = this.organizationService.getOrganizationsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;
    }

    @GetMapping("/organizations/desc")
    public ModelAndView showOrganizationsDesc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Organization> pageWrapper =
                new PageWrapper<>(this.organizationService.retrieveSortedOrganizationsDesc(pageable),
                                                                       "/admin/organizations/desc");
        Long limit = this.organizationService.getOrganizationsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;
    }

    @PostMapping("/organizations/add")
    public ModelAndView addOrganization(@RequestParam("title") String title, @RequestParam("type") String type){

        this.organizationService.addOrganization(title, type);

        return new ModelAndView("redirect:/admin/organizations");

    }

    @GetMapping("/organizations/show/{id}")
    public ModelAndView showOrganization(@PathVariable("id") Long id){

        Organization organization = this.organizationService.findById(id);
        Set<Event> events = this.organizationService.getEvents(id);
        Set<Student> participants = this.organizationService.getParticipants(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("organization", organization);
        modelAndView.addObject("events", events);
        modelAndView.addObject("students", participants);
        modelAndView.addObject("peopleCount", this.organizationService.getNumberOfPeople(id));
        modelAndView.setViewName("organizations/view-organization");

        return modelAndView;

    }

    @GetMapping("/organizations/edit/{id}")
    public ModelAndView editOrganization(@PathVariable("id") Long id){

        Set<Event> events = this.organizationService.getEvents(id);
        Set<Student> participants = this.organizationService.getParticipants(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("organizations/edit-organization");
        modelAndView.addObject("events", events);
        modelAndView.addObject("students", participants);
        modelAndView.addObject("id", id);

        return modelAndView;
    }

    @GetMapping("/organizations/{organizationId}/delete/{eventId}")
    public ModelAndView deleteEvent(@PathVariable("organizationId") Long organizationId,
                                    @PathVariable("eventId") Long eventId){

        this.organizationService.deleteEvent(organizationId, eventId);

        return new ModelAndView("redirect:/admin/organizations/edit/" + organizationId);

    }

    @GetMapping("/organizations/{organizationId}/delete/{participantId}")
    public ModelAndView deleteParticipant(@PathVariable("organizationId") Long organizationId,
                                          @PathVariable("participantId") Long participantId){

        this.organizationService.deleteParticipant(organizationId, participantId);

        return new ModelAndView("redirect:/admin/organizations/edit/" + organizationId);

    }

    @PostMapping("/organizations/edit")
    public ModelAndView updateOrganization(@ModelAttribute("organizationForm") OrganizationForm organizationForm){

        this.organizationService.updateOrganization(organizationForm);

        return new ModelAndView("redirect:/admin/organizations/edit/" + organizationForm.getId());

    }

    @PostMapping("/organizations/search")
    public ModelAndView searchOrganization(@RequestParam("search") String searchQuery, @PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Organization> pageWrapper =
                new PageWrapper<>(this.organizationService.searchByTitle(searchQuery, pageable), "/admin/organizations/search");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-organizations");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;

    }

}
