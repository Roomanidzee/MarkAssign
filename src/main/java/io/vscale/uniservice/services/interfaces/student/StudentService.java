package io.vscale.uniservice.services.interfaces.student;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Group;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.forms.general.NewEventForm;
import io.vscale.uniservice.forms.general.StudentForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface StudentService {

    List<Student> getStudentsByGroup(Group group);
    List<Student> getAllStudents();
    Student getStudentById(Long studentId);
    Page<Student> findAll(Pageable pageable);
    Page<Student> retrieveSortedStudentsAsc(Pageable pageable);
    Page<Student> retrieveSortedStudentsDesc(Pageable pageable);

    void addStudent(StudentForm studentForm);
    void updateStudent(StudentForm studentForm);
    void deleteStudent(StudentForm studentForm);

    void addEvent(NewEventForm newEventForm);
    List<Event> getStudentEvents(Student student);

    List<FileOfService> getConfirmationFiles(Long studentId);
    Page<Student> searchBySurname(String surname);
    Long getStudentsCount();
    Long getStudentsMarks(Long studentId);

}
