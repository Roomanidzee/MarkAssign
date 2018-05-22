package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.services.interfaces.events.EventService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import io.vscale.uniservice.utils.FormDataParser;
import io.vscale.uniservice.utils.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 07.05.2018
 *
 * @author Andrey Romanov, Dias Arkharov, Aynur Aymurzin
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class ScoreController {

    private final EventService eventService;
    private final StudentService studentService;
    private final FormDataParser formDataParser;

    @Autowired
    public ScoreController(EventService eventService, StudentService studentService, FormDataParser formDataParser) {
        this.eventService = eventService;
        this.studentService = studentService;
        this.formDataParser = formDataParser;
    }

    @GetMapping("/scores/add")
    public ModelAndView addScores(){
        List<Event> events =  eventService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("events", events);
        modelAndView.setViewName("scores/add-scores");
        return modelAndView;
    }

    @PostMapping("/scores/save")
    public ModelAndView addScoresAndConfirmation(HttpServletRequest request){
        formDataParser.parse(request.getParameterNames(), request);
        return new ModelAndView();
    }

    @GetMapping("/scores/distribute")
    public ModelAndView distributeScores(){
        return new ModelAndView("scores/distribute-scores");
    }

    @GetMapping("/scores/edit")
    public ModelAndView editScores(){
        List<Event> events =  eventService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("events", events);
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
    public ModelAndView viewScores(){
        List<Event> events =  eventService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("events", events);
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
