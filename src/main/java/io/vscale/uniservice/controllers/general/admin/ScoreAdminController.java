package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.*;
import io.vscale.uniservice.services.interfaces.auth.AuthenticationService;
import io.vscale.uniservice.services.interfaces.events.EventService;
import io.vscale.uniservice.services.interfaces.events.EventTypeEvaluationService;
import io.vscale.uniservice.services.interfaces.student.GroupService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import io.vscale.uniservice.utils.FormDataParser;
import io.vscale.uniservice.utils.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 07.05.2018
 *
 * @author Andrey Romanov, Dias Arkharov, Aynur Aymurzin
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class ScoreAdminController {

    private final EventService eventService;
    private final EventTypeEvaluationService eventTypeEvaluationService;
    private final StudentService studentService;
    private final FormDataParser formDataParser;
    private final AuthenticationService authenticationService;
    private final GroupService groupService;

    @Autowired
    public ScoreAdminController(EventService eventService, EventTypeEvaluationService eventTypeEvaluationService, StudentService studentService, FormDataParser formDataParser,
                                @Qualifier("generalAuthenticationService") AuthenticationService authenticationService,
                                GroupService groupService) {
        this.eventService = eventService;
        this.eventTypeEvaluationService = eventTypeEvaluationService;
        this.studentService = studentService;
        this.formDataParser = formDataParser;
        this.authenticationService = authenticationService;
        this.groupService = groupService;
    }

    @GetMapping("/scores/add")
    public ModelAndView addScores(){

        List<EventTypeEvaluation> evaluations = this.eventTypeEvaluationService.getAllEvaluations();
        List<Group> groups = this.groupService.getAllGroups();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("evaluations", evaluations);
        modelAndView.addObject("groups", groups);
        modelAndView.setViewName("scores/add-scores");

        return modelAndView;
    }

    @PostMapping("/scores/save")
    public ModelAndView addScoresAndConfirmation(HttpServletRequest request){
        formDataParser.parse(request.getParameterNames(), request);
        return new ModelAndView();
    }

    @GetMapping("/scores/edit")
    public ModelAndView editScores(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        assert user.getProfile().getStudent() != null : "Authentication error";

        Student student = user.getProfile().getStudent();
        Set<EventTypeEvaluation> evaluations = student.getEvaluations();

        List<Event> events =  student.getEvents();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("events", events);
        modelAndView.addObject("evaluations", evaluations);
        modelAndView.setViewName("scores/edit-scores");
        return modelAndView;
    }

    @GetMapping("/scores/delete/{id}")
    public ModelAndView editScores(@PathVariable("id") Long id){
        Event event = eventService.findOne(id);
        eventService.delete(event);
        return new ModelAndView("redirect:/admin/scores/edit");
    }

    @GetMapping("/scores/view")
    public ModelAndView viewScores(Authentication authentication){
        User user = this.authenticationService.getUserByAuthentication(authentication);
        assert user.getProfile().getStudent() != null : "Authentication error";

        Student student = user.getProfile().getStudent();
        Long marks = this.studentService.getStudentsMarks(student.getId());

        List<Event> events =  student.getEvents();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("events", events);
        modelAndView.addObject("marks", marks);
        modelAndView.setViewName("scores/view-scores");

        return modelAndView;
    }

    @GetMapping("/scores/view-students")
    public ModelAndView viewStudentsScores(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Student> pageWrapper =
                new PageWrapper<>(this.studentService.findAll(pageable), "/admin/scores/view-students");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.setViewName("scores/view-students-scores");

        return modelAndView;

    }

}
