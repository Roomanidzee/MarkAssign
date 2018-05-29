package io.vscale.uniservice.services.interfaces.events;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.EventTypeEvaluation;

import java.util.List;
import java.util.Map;

/**
 * 04.05.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface EventTypeEvaluationService {

    List<EventTypeEvaluation> getAllEvaluations();
    EventTypeEvaluation getEvaluationByType(String type);
    EventTypeEvaluation getEvaluationByEvent(Event event);
    Map<Integer, String> castMapWithEvaluations(Map<String, String> requestForm);
    void distributeEvaluations(Map<Integer, String> evaluationToSubjects, Long studentId);
    void addStudentEvaluation(Long studentId, Long eventId, String studentRole);
    boolean checkStudent(Long evaluationId, Long studentId);
    void deleteEventFromEvaluation(Long evaluationId, Long eventId);

}
