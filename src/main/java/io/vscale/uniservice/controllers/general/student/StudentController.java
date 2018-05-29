package io.vscale.uniservice.controllers.general.student;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.services.interfaces.events.EventService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import io.vscale.uniservice.utils.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 29.05.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final EventService eventService;

    @Autowired
    public StudentController(StudentService studentService, EventService eventService) {
        this.studentService = studentService;
        this.eventService = eventService;
    }

    @GetMapping("/index")
    public ModelAndView getStudentIndexPage(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.findAll(pageable), "/student/index");
        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @GetMapping("/events")
    public ModelAndView showEvents(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.findAll(pageable), "/student/events");

        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @GetMapping("/events/asc")
    public ModelAndView showEventsAsc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.retrieveAllEventsAsc(pageable), "/student/events/asc");

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
                new PageWrapper<>(this.eventService.retrieveAllEventsDesc(pageable), "/student/events/desc");
        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @PostMapping("/events/search")
    public ModelAndView searchEvents(@RequestParam("search") String searchQuery){

        PageWrapper<Event> pageWrapper =
                new PageWrapper<>(this.eventService.searchByTitle(searchQuery), "/student/events/search");
        Long limit = this.eventService.getEventsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-events");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

        return modelAndView;

    }

    @GetMapping("/event/view/{id}")
    public ModelAndView viewEvent(@PathVariable("id") Long id){

        Event event = this.eventService.findOne(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("events/view-event");
        modelAndView.addObject("event_model", event);
        modelAndView.addObject("students_size", event.getStudents().size());

        return modelAndView;
    }
}
