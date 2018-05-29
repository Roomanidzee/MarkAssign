package io.vscale.uniservice.controllers.rest.admin;

import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.forms.general.StudentForm;
import io.vscale.uniservice.forms.rest.StudentRESTForm;
import io.vscale.uniservice.services.interfaces.admin.StudentAdminService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import io.vscale.uniservice.validators.StudentFormValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 17.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@RestController
@RequestMapping("/api_v1/admin_role")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentAdminRESTController {

    private StudentFormValidator studentFormValidator;
    private StudentAdminService studentAdminService;
    private StudentService studentService;

    @InitBinder
    public void initUserFormValidator(WebDataBinder binder){
        binder.addValidators(this.studentFormValidator);
    }

    @PostMapping("/create_student")
    public List<Student> createStudent(@Valid @ModelAttribute("studentForm")StudentRESTForm studentForm){
        this.studentAdminService.makeRESTStudent(studentForm);
        return this.studentService.getAllStudents();
    }

    @PostMapping("/update_student")
    public ResponseEntity<List<Student>> updateStudent(Long id, @Valid @ModelAttribute("studentForm")StudentForm studentForm){

        this.studentService.updateStudent(id, studentForm);
        return ResponseEntity.ok(this.studentService.getAllStudents());

    }

    @PostMapping("/delete_student")
    public ResponseEntity<List<Student>> deleteStudent(Long id){

        this.studentService.deleteStudent(id);
        return ResponseEntity.ok(this.studentService.getAllStudents());

    }

    @GetMapping("/students/asc")
    public ResponseEntity<Page<Student>> getStudentsAsc(Pageable pageable){
        return ResponseEntity.ok(this.studentService.retrieveSortedStudentsAsc(pageable));
    }

    @GetMapping("/students/desc")
    public ResponseEntity<Page<Student>> getStudentsDesc(Pageable pageable){
        return ResponseEntity.ok(this.studentService.retrieveSortedStudentsDesc(pageable));
    }

}
