package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.services.interfaces.events.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 07.05.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class ScoreController {

    private final EventService eventService;

    @Autowired
    public ScoreController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/scores/add")
    public ModelAndView addScores(){
        return new ModelAndView("scores/add-scores");
    }

    @GetMapping("/scores/distribute")
    public ModelAndView distributeScores(){
        return new ModelAndView("scores/distribute-scores");
    }

    @GetMapping("/scores/edit")
    public ModelAndView editScores(){
        return new ModelAndView("scores/edit-scores");
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
    public ModelAndView viewStudentsScores(){
        return new ModelAndView("scores/view-students-scores");
    }

}
