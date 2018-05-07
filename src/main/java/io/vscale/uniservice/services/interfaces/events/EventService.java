package io.vscale.uniservice.services.interfaces.events;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.EventTypeEvaluation;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.dto.EventDTO;
import io.vscale.uniservice.forms.general.EventTypeEvaluationForm;
import io.vscale.uniservice.forms.general.NewEventForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 01.03.2018
 *
 * @author Aynur Aymurzin
 * @version 1.0
 */
public interface EventService {
    List<Event> findByTypeEvaluation(EventTypeEvaluation typeEvaluation);
    void delete(Event event);
    Event save(Event event);
    Event findOneByEventTypeEvaluations(EventTypeEvaluation evaluation);
    Event findOne(Long id);
    List<Event> findAll();
    Page<Event> findAll(Pageable pageable);
    boolean getNewEvents();
    void addEventWithChecking(NewEventForm newEventForm);
    List<EventDTO> getEventsDTO();
    List<Event> getEventsByUser(User user);
    void addEvaluationToEvent(EventTypeEvaluationForm eventTypeEvaluationForm);
    void addFileOfService(Event event, MultipartFile multipartFile);
}
