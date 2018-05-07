package io.vscale.uniservice.controllers.general.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 07.05.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class ScoreController {

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
        return new ModelAndView("scores/view-scores");
    }

    @GetMapping("/scores/view-students")
    public ModelAndView viewStudentsScores(){
        return new ModelAndView("scores/view-students-scores");
    }

}
