package io.vscale.uniservice.controllers.general.admin;

import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.forms.general.StudentForm;
import io.vscale.uniservice.services.interfaces.admin.StudentAdminService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import io.vscale.uniservice.utils.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
public class StudentAdminController {

    private StudentService studentService;
    private StudentAdminService studentAdminService;

    @GetMapping("/students")
    public ModelAndView showStudents(@PageableDefault(value = 4) Pageable pageable){

        PageWrapper<Student> pageWrapper =
                new PageWrapper<>(this.studentService.findAll(pageable), "/admin/students");
        Long limit = this.studentService.getStudentsCount();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-students");
        modelAndView.addObject("pageWrapper", pageWrapper);
        modelAndView.addObject("limit", limit);

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

    @PostMapping("/students/add")
    public ModelAndView addStudent(@ModelAttribute("studentForm") StudentForm studentForm){

        this.studentService.addStudent(studentForm);

        return new ModelAndView("redirect:/admin/students");

    }

    @GetMapping("/students/edit")
    public ModelAndView editStudent(){
        return new ModelAndView("students/edit-student");
    }

    @GetMapping("/students/view/{id}")
    public ModelAndView viewStudent(@PathVariable("id") Long id){

        Student student = this.studentService.getStudentById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("students/view-student");
        modelAndView.addObject("student", student);

        return new ModelAndView("students/view-student");

    }

    @PostMapping("/students/search")
    public ModelAndView searchStudents(@RequestParam("search") String searchQuery){

        PageWrapper<Student> pageWrapper =
                new PageWrapper<>(this.studentService.searchBySurname(searchQuery), "/admin/students/search");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/admin-students");
        modelAndView.addObject("pageWrapper", pageWrapper);

        return modelAndView;

    }

}
