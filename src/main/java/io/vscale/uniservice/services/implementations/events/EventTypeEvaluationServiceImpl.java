package io.vscale.uniservice.services.implementations.events;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.EventTypeEvaluation;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.domain.SubjectsToCourse;
import io.vscale.uniservice.repositories.data.EventRepository;
import io.vscale.uniservice.repositories.data.EventTypeEvaluationRepository;
import io.vscale.uniservice.repositories.data.StudentRepository;
import io.vscale.uniservice.repositories.data.SubjectsToCourseRepository;
import io.vscale.uniservice.services.interfaces.events.EventTypeEvaluationService;
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
public class EventTypeEvaluationServiceImpl implements EventTypeEvaluationService{

    private final EventTypeEvaluationRepository eventTypeEvaluationRepository;
    private final StudentRepository studentRepository;
    private final SubjectsToCourseRepository subjectsToCourseRepository;

    @Autowired
    public EventTypeEvaluationServiceImpl(EventTypeEvaluationRepository eventTypeEvaluationRepository,
                                          StudentRepository studentRepository,
                                          SubjectsToCourseRepository subjectsToCourseRepository) {
        this.eventTypeEvaluationRepository = eventTypeEvaluationRepository;
        this.studentRepository = studentRepository;
        this.subjectsToCourseRepository = subjectsToCourseRepository;
    }

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
}
