package io.vscale.uniservice.services.implementations.student;

import io.vscale.uniservice.domain.*;
import io.vscale.uniservice.forms.general.NewEventForm;
import io.vscale.uniservice.forms.general.StudentForm;
import io.vscale.uniservice.repositories.data.EventRepository;
import io.vscale.uniservice.repositories.data.ProfileRepository;
import io.vscale.uniservice.repositories.indexing.EventESRepository;
import io.vscale.uniservice.repositories.indexing.StudentESRepository;
import lombok.AllArgsConstructor;

import io.vscale.uniservice.repositories.data.StudentRepository;
import io.vscale.uniservice.services.interfaces.student.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private EventRepository eventRepository;
    private ProfileRepository profileRepository;
    private StudentESRepository studentESRepository;
    private EventESRepository eventESRepository;

    @Override
    public List<Student> getStudentsByGroup(Group group) {

        List<Student> currentStudents = this.studentRepository.findAll();

        return currentStudents.stream()
                              .filter(student -> student.getGroups().equals(group))
                              .collect(Collectors.toList());

    }

    @Override
    public List<Student> getAllStudents() {
        return this.studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long studentId) {
        return this.studentRepository.findOne(studentId);
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return this.studentRepository.findAll(pageable);
    }

    @Override
    public Page<Student> retrieveSortedStudentsAsc(Pageable pageable) {

        Long number;

        if(pageable.getPageNumber() == 1){
            number = (long)0;
        }else{
            number = (long) (pageable.getPageNumber() + 3);
        }

        List<Student> students = this.studentRepository.findAllByOrderBySurnameAsc(number);

        return new PageImpl<>(students, pageable, students.size());

    }

    @Override
    public Page<Student> retrieveSortedStudentsDesc(Pageable pageable) {

        Long number;

        if(pageable.getPageNumber() == 1){
            number = (long)0;
        }else{
            number = (long) (pageable.getPageNumber() + 3);
        }

        List<Student> students = this.studentRepository.findAllByOrderBySurnameDesc(number);

        return new PageImpl<>(students, pageable, students.size());

    }

    @Override
    public void addStudent(StudentForm studentForm) {

        Profile profile = this.profileRepository.findOne(studentForm.getProfileId());

        Student student = Student.builder()
                                 .profile(profile)
                                 .course(studentForm.getCourse())
                                 .gender(studentForm.getGender())
                                 .headOrganizations(new HashSet<>())
                                 .events(new ArrayList<>())
                                 .confirmations(new HashSet<>())
                                 .groups(new HashSet<>())
                                 .build();

        this.studentRepository.save(student);
        this.studentESRepository.save(student);

    }

    @Override
    public void updateStudent(StudentForm studentForm) {

        Student student = this.studentRepository.findOne(studentForm.getId());
        Profile profile = this.profileRepository.findOne(studentForm.getProfileId());

        student.setCourse(studentForm.getCourse());
        student.setGender(studentForm.getGender());
        student.setProfile(profile);

        this.studentRepository.save(student);
        this.studentESRepository.save(student);


    }

    @Override
    public void deleteStudent(StudentForm studentForm) {

        this.studentRepository.delete(studentForm.getId());
        this.studentESRepository.delete(String.valueOf(studentForm.getId()));

    }

    @Override
    public void addEvent(NewEventForm newEventForm) {

        Event event = Event.builder()
                           .name(newEventForm.getTitle())
                           .eventDate(Timestamp.valueOf(newEventForm.getEventDate()))
                           .description(newEventForm.getDescription())
                           .build();

        this.eventRepository.save(event);
        this.eventESRepository.save(event);

        Student student = this.studentRepository.findOne(newEventForm.getStudentId());

        if(student.getEvents() == null){
            student.setEvents(Collections.singletonList(event));
        }else{
            student.getEvents().add(event);
        }

        this.studentRepository.save(student);
        this.studentESRepository.save(student);

    }

    @Override
    public List<Event> getStudentEvents(Student student) {
        return student.getEvents();
    }

    @Override
    public Integer getMarksSum(Student student) {

        AtomicInteger result = new AtomicInteger();
        result.set(0);

        List<Event> events = student.getEvents();

        events.forEach(event ->
                            result.set(result.get() + event.getEventTypeEvaluations()
                                                           .stream()
                                                           .mapToInt(EventTypeEvaluation::getFinalValue)
                                                           .sum())
        );

        return result.get();

    }

    @Override
    public List<FileOfService> getConfirmationFiles(Long studentId) {

        Student student = this.studentRepository.findOne(studentId);
        Set<Confirmation> studentConfirmations = student.getConfirmations();

        return studentConfirmations.stream()
                                   .map(Confirmation::getFileOfService)
                                   .filter(Objects::nonNull)
                                   .collect(Collectors.toList());

    }
}