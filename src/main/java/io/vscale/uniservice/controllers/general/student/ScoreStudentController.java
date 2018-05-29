package io.vscale.uniservice.controllers.general.student;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.services.interfaces.auth.AuthenticationService;
import io.vscale.uniservice.services.interfaces.events.EventService;
import io.vscale.uniservice.services.interfaces.events.EventTypeEvaluationService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
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
public class ScoreStudentController {

    private final EventTypeEvaluationService eventTypeEvaluationService;
    private final AuthenticationService authenticationService;
    private final StudentService studentService;
    private final EventService eventService;

    @Autowired
    public ScoreStudentController(EventTypeEvaluationService eventTypeEvaluationService,
                                  @Qualifier("generalAuthenticationService") AuthenticationService authenticationService,
                                  StudentService studentService,
                                  EventService eventService) {
        this.eventTypeEvaluationService = eventTypeEvaluationService;
        this.authenticationService = authenticationService;
        this.studentService = studentService;
        this.eventService = eventService;
    }

    @GetMapping("/add_role/{studentId}/{eventId}")
    public ModelAndView getRolePage(@PathVariable("studentId") Long studentId, @PathVariable("eventId") Long eventId){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("studentId", studentId);
        modelAndView.addObject("eventId", eventId);
        modelAndView.setViewName("scores/add-role");

        return modelAndView;

    }

    @PostMapping("/add_role")
    public ModelAndView addStudentRole(@RequestParam("studentId") Long studentId, @RequestParam("eventId") Long eventId,
                                       @RequestParam("role") String role){

        this.eventTypeEvaluationService.addStudentEvaluation(studentId, eventId, role);

        return new ModelAndView("redirect:/student/list-events");

    }

    @GetMapping("/view-scores")
    public ModelAndView getStudentScores(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Student student = user.getProfile().getStudent();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("scores/view-scores");
        modelAndView.addObject("student", student);
        modelAndView.addObject("evaluations", student.getEvaluations());
        modelAndView.addObject("marks", this.studentService.getStudentsMarks(student.getId()));

        return modelAndView;

    }

    @GetMapping("/edit-scores")
    public ModelAndView getEditScoresPage(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Student student = user.getProfile().getStudent();
        List<Event> events = this.eventService.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("student", student);
        modelAndView.addObject("evaluations", student.getEvaluations());
        modelAndView.addObject("events", events);
        modelAndView.setViewName("scores/edit-scores");

        return modelAndView;

    }

    @GetMapping("/delete/evaluation/{evaluationId}/event/{eventId}")
    public ModelAndView deleteEvaluation(@PathVariable("evaluationId") Long evaluationId,
                                         @PathVariable("eventId") Long eventId){

        this.eventTypeEvaluationService.deleteEventFromEvaluation(evaluationId, eventId);
        return new ModelAndView("redirect:/student/edit-scores");

    }

    @GetMapping("/scores/distribute")
    public ModelAndView distributeScores(Authentication authentication){

        User user = this.authenticationService.getUserByAuthentication(authentication);
        Student student = user.getProfile().getStudent();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("scores/distribute-scores");
        modelAndView.addObject("marks", this.studentService.getStudentsMarks(student.getId()));

        return modelAndView;
    }

}
