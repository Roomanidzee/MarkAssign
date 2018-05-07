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
public class EmployeeController {

    @GetMapping("/employee/edit")
    public ModelAndView editEmployee(){
        return new ModelAndView("employees/edit-employee");
    }

    @GetMapping("/employee/view")
    public ModelAndView viewEmployee(){
        return new ModelAndView("employees/view-employee");
    }

}
