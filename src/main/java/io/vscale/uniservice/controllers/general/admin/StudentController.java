package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.services.interfaces.admin.StudentAdminService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import io.vscale.uniservice.utils.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import lombok.AllArgsConstructor;

/**
 * 05.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    private StudentService studentService;
    private StudentAdminService studentAdminService;

    @GetMapping("/students")
    public ModelAndView showStudents(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Student> pageWrapper =
                new PageWrapper<>(this.studentService.findAll(pageable), "/admin/students");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-students");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;

    }

    @GetMapping("/students/asc")
    public ModelAndView showStudentsAsc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Student> pageWrapper =
                new PageWrapper<>(this.studentService.retrieveSortedStudentsAsc(pageable), "/admin/students/asc");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-students");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;

    }

    @GetMapping("/students/desc")
    public ModelAndView showStudentsDesc(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Student> pageWrapper =
                new PageWrapper<>(this.studentService.retrieveSortedStudentsDesc(pageable), "/admin/students/desc");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-students");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;

    }

    @GetMapping("/students/edit")
    public ModelAndView editStudent(){
        return new ModelAndView("students/edit-student");
    }

    @GetMapping("/students/view")
    public ModelAndView viewStudent(){
        return new ModelAndView("students/view-student");
    }

}
