package io.vscale.uniservice.controllers.rest.student;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.Group;
import io.vscale.uniservice.domain.Student;
import lombok.AllArgsConstructor;

import io.vscale.uniservice.services.interfaces.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 04.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@RestController
@RequestMapping("/api_v1")
public class StudentRESTController {

    private final StudentService studentService;

    @Autowired
    public StudentRESTController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student_role/show/group")
    public List<Student> getStudentByGroup(@RequestBody Group group){
        return this.studentService.getStudentsByGroup(group);
    }

    @GetMapping({"/student_role/student/{id}", "/admin_role/student/{id}"})
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.studentService.getStudentById(id));
    }

    @GetMapping("/student/mark")
    public ResponseEntity<Long> getStudentMarks(Student student){
        return ResponseEntity.ok(this.studentService.getStudentsMarks(student.getId()));
    }

    @GetMapping({"/admin_role/student/events", "/student_role/student/events"})
    public ResponseEntity<List<Event>> getStudentEvents(Student student){
        return ResponseEntity.ok(this.studentService.getStudentEvents(student));
    }

}
