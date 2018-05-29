package io.vscale.uniservice.services.implementations.events;

import io.vscale.uniservice.domain.*;
import io.vscale.uniservice.repositories.data.*;
import io.vscale.uniservice.services.interfaces.events.EventTypeEvaluationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 04.05.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventTypeEvaluationServiceImpl implements EventTypeEvaluationService{

    private final EventTypeEvaluationRepository eventTypeEvaluationRepository;
    private final StudentRepository studentRepository;
    private final SubjectsToCourseRepository subjectsToCourseRepository;
    private final EventRepository eventRepository;

    @Override
    public List<EventTypeEvaluation> getAllEvaluations() {
        return this.eventTypeEvaluationRepository.findAll();
    }

    @Override
    public EventTypeEvaluation getEvaluationByType(String type) {
        return this.eventTypeEvaluationRepository.findByType(type);
    }

    @Override
    public EventTypeEvaluation getEvaluationByEvent(Event event) {
        return this.eventTypeEvaluationRepository.findByEventsContaining(event);
    }

    @Override
    public Map<Integer, String> castMapWithEvaluations(Map<String, String> requestForm) {

        return requestForm.entrySet()
                          .stream()
                          .collect(Collectors.toMap(entry -> Integer.valueOf(entry.getKey()),
                                                    Map.Entry::getValue,
                                                   (a, b) -> b));
    }

    @Override
    public void distributeEvaluations(Map<Integer, String> evaluationToSubjects, Long studentId) {

        Student student = this.studentRepository.findOne(studentId);

        evaluationToSubjects.forEach((markValue, subjectName) -> {

            SubjectsToCourse subject = this.subjectsToCourseRepository.findBySubjectName(subjectName);

            EventTypeEvaluation eventTypeEvaluation =
                    EventTypeEvaluation.builder()
                                       .type("Оценивание для студента с ID: " + student.getId())
                                       .finalValue((byte)markValue.intValue())
                                       .subjects(Collections.singleton(subject))
                                       .build();
            this.eventTypeEvaluationRepository.save(eventTypeEvaluation);
            student.getEvaluations().add(eventTypeEvaluation);

        });

        this.studentRepository.save(student);

    }

    @Override
    public void addStudentEvaluation(Long studentId, Long eventId, String studentRole) {

        Student student = this.studentRepository.findOne(studentId);

        if(student == null){
            throw new IllegalArgumentException("No such student");
        }

        Event event = this.eventRepository.findOne(eventId);
        String type = event.getEventTypeName();
        Byte startValue = event.getEventTypeEvaluations().get(0).getStartValue();
        Byte endValue = event.getEventTypeEvaluations().get(1).getEndValue();

        EventTypeEvaluation eventTypeEvaluation = EventTypeEvaluation.builder()
                                                                     .type(type)
                                                                     .startValue(startValue)
                                                                     .endValue(endValue)
                                                                     .students(Collections.singletonList(student))
                                                                     .studentRole(studentRole)
                                                                     .build();

        this.eventTypeEvaluationRepository.save(eventTypeEvaluation);


    }

    @Override
    public boolean checkStudent(Long evaluationId, Long studentId) {

        EventTypeEvaluation eventTypeEvaluation = this.eventTypeEvaluationRepository.findOne(evaluationId);

        return eventTypeEvaluation.getStudents().stream()
                                                .anyMatch(student -> student.getId().equals(studentId));

    }

    @Override
    public void deleteEventFromEvaluation(Long evaluationId, Long eventId) {

        EventTypeEvaluation eventTypeEvaluation = this.eventTypeEvaluationRepository.findOne(evaluationId);

        eventTypeEvaluation.getEvents().removeIf(event -> event.getId().equals(eventId));

    }


}
