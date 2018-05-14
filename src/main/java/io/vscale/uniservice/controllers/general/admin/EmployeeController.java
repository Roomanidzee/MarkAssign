package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.forms.general.CooperatorForm;
import io.vscale.uniservice.services.interfaces.cooperator.CooperatorService;
import io.vscale.uniservice.validators.CooperatorFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
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

    private final CooperatorFormValidator cooperatorFormValidator;
    private final CooperatorService cooperatorService;

    @Autowired
    public EmployeeController(CooperatorFormValidator cooperatorFormValidator, CooperatorService cooperatorService) {
        this.cooperatorFormValidator = cooperatorFormValidator;
        this.cooperatorService = cooperatorService;
    }

    @InitBinder("cooperatorForm")
    public void initValidator(WebDataBinder binder){
        binder.addValidators(this.cooperatorFormValidator);
    }

    @PostMapping("/employee/add")
    public ModelAndView addEmployee(@Validated @ModelAttribute("cooperatorForm") CooperatorForm cooperatorForm){

        this.cooperatorService.addCooperator(cooperatorForm);
        return new ModelAndView("redirect:/admin/cooperators");

    }

    @GetMapping("/employee/edit")
    public ModelAndView editEmployeePage(){
        return new ModelAndView("employees/edit-employee");
    }

    @PostMapping("/employee/edit")
    public ModelAndView editEmployee(@Validated @ModelAttribute("cooperatorForm") CooperatorForm cooperatorForm){

        this.cooperatorService.updateCooperator(cooperatorForm);
        return new ModelAndView("redirect:/admin/employee/view");

    }

    @GetMapping("/employee/{id}/view")
    public ModelAndView viewEmployee(@PathVariable("id") Long id){

        Cooperator cooperator = this.cooperatorService.getCooperatorById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employees/view-employee");
        modelAndView.addObject("cooperator", cooperator);

        return modelAndView;
    }

}
