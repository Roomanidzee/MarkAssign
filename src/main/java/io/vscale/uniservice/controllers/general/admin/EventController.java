package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.forms.general.NewEventForm;
import io.vscale.uniservice.services.interfaces.events.EventService;
import io.vscale.uniservice.utils.PageWrapper;
import io.vscale.uniservice.validators.NewEventFormValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 26.03.2018
 *
 * @author Aynur Aymurzin
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventController {

    private EventService eventService;
    private NewEventFormValidator newEventFormValidator;

    @InitBinder("newEventForm")
    public void initValidator(WebDataBinder binder){
        binder.addValidators(this.newEventFormValidator);
    }

    @GetMapping("/events")
    public ModelAndView showEvents(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.findAll(pageable), "/admin/events");

        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @GetMapping("/events/alph")
    public ModelAndView showEventsWithAlthabeticPagination(@RequestParam(defaultValue = "а") char ch){

        String start = ""+ ch;
        List<Event> events = eventService.findAll();
        List<Event> res = events.stream()
                                .filter( o -> o.getName().toLowerCase().startsWith(start))
                                .collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();

        List<Character> characters = new ArrayList<>();
        for (int i =0; i < 32; i++){
            characters.add((char) (i + 'а'));
        }
        modelAndView.setViewName("admin/admin-events-alph");
        modelAndView.addObject("res", res);
        modelAndView.addObject("curentCh", ch);
        modelAndView.addObject("alph", characters);

        return modelAndView;

    }

    @GetMapping("/events/asc")
    public ModelAndView showEventsAsc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.retrieveAllEventsAsc(pageable), "/admin/events/asc");

        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @GetMapping("/events/desc")
    public ModelAndView showEventsDesc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.retrieveAllEventsDesc(pageable), "/admin/events/desc");

        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @GetMapping("/event/{eventId}/edit")
    public ModelAndView editEvent(@PathVariable("eventId") long eventId){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/events/edit-event");
        Event event = eventService.findOne(eventId);

        Set<FileOfService> files = event.getFiles();

        Set<FileOfService> applicationTypeFiles = files.stream()
                                                       .filter(o -> o.getType().startsWith("image"))
                                                       .collect(Collectors.toSet());

        mav.addObject("event_model", event);
        mav.addObject("files", applicationTypeFiles);
        mav.addObject("students_size", event.getStudents().size());
        return mav;

    }

    @GetMapping("/event/{eventId}/view")
    public ModelAndView viewEvent(@PathVariable("eventId") long eventId){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("events/view-event");
        Event event = eventService.findOne(eventId);

        Set<FileOfService> files = event.getFiles();

        Set<FileOfService> photos = files.stream()
                                         .filter(o -> o.getType().startsWith("image"))
                                         .collect(Collectors.toSet());

        Set<FileOfService> applicationTypeFiles = files.stream()
                                                       .filter(o -> o.getType().startsWith("image"))
                                                       .collect(Collectors.toSet());

        mav.addObject("photos", photos);
        mav.addObject("files", applicationTypeFiles);
        mav.addObject("event_model", event);
        mav.addObject("students_size", event.getStudents().size());
        return mav;
    }

    @PostMapping("/events/{eventId}/edit/files")
    public ModelAndView postEventFiles(@PathVariable long eventId, @RequestParam("file") MultipartFile multipartFile){
        Event event = eventService.findOne(eventId);
        eventService.addFileOfService(event, multipartFile);
        return new ModelAndView("redirect:/admin/events/"+eventId + "/edit");
    }

    @PostMapping("/events/search")
    public ModelAndView searchEvents(@RequestParam("search") String searchQuery){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.searchByTitle(searchQuery), "/admin/events/search");
        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @PostMapping("/events/add")
    public ModelAndView addEvent(@Validated NewEventForm newEventForm){
        this.eventService.addEventWithChecking(newEventForm);
        return new ModelAndView("redirect:/admin/events");
    }

}
