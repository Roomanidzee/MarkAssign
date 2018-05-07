package io.vscale.uniservice.services.implementations.events;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.EventTypeEvaluation;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.dto.EventDTO;
import io.vscale.uniservice.forms.general.EventTypeEvaluationForm;
import io.vscale.uniservice.forms.general.NewEventForm;
import io.vscale.uniservice.repositories.data.EventRepository;
import io.vscale.uniservice.repositories.data.EventTypeEvaluationRepository;
import io.vscale.uniservice.repositories.data.StudentRepository;
import io.vscale.uniservice.services.interfaces.events.EventService;
import io.vscale.uniservice.services.interfaces.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 01.03.2018
 *
 * @author Aynur Aymurzin
 * @version 1.0
 */
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final StudentRepository studentRepository;
    private final StorageService storageService;
    private final EventTypeEvaluationRepository eventTypeEvaluationRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, StudentRepository studentRepository,
                            StorageService storageService, EventTypeEvaluationRepository eventTypeEvaluationRepository){
        this.repository = eventRepository;
        this.studentRepository = studentRepository;
        this.storageService = storageService;
        this.eventTypeEvaluationRepository = eventTypeEvaluationRepository;
    }

    @Override
    public List<Event> findByTypeEvaluation(EventTypeEvaluation typeEvaluation) {

        List<Event> currentEvents = this.repository.findAll();

        return currentEvents.stream()
                            .filter(event -> event.getEventTypeEvaluations().equals(typeEvaluation))
                            .collect(Collectors.toList());

    }

    @Override
    public void delete(Event event) {
        repository.delete(event);
    }

    @Override
    public Event save(Event event) {
        return repository.save(event);
    }

    @Override
    public Event findOneByEventTypeEvaluations(EventTypeEvaluation evaluation) {
        return repository.findOneByEventTypeEvaluations(Collections.singletonList(evaluation));
    }

    @Override
    public Event findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public boolean getNewEvents() {

        List<Event> currentEvents = this.repository.findAll();
        LocalDate localDateCheck = LocalDate.now();

        return currentEvents.stream()
                            .anyMatch(event -> event.getTimestamp().equals(
                                                           Timestamp.valueOf(localDateCheck.atStartOfDay())
                            ));

    }

    @Override
    public void addEventWithChecking(NewEventForm newEventForm) {

        Event event = Event.builder()
                           .description(newEventForm.getTitle())
                           .timestamp(Timestamp.valueOf(newEventForm.getEventDate()))
                           .students(Collections.singleton(this.studentRepository.findOne(newEventForm.getStudentId())))
                           .build();

        this.repository.save(event);

    }

    @Override
    public List<EventDTO> getEventsDTO() {
        return repository.findAllEventsAsDTO();
    }

    @Override
    public List<Event> getEventsByUser(User user) {
        return user.getProfile()
                   .getStudent()
                   .getEvents();
    }

    @Override
    public void addEvaluationToEvent(EventTypeEvaluationForm eventTypeEvaluationForm) {

        EventTypeEvaluation eventTypeEvaluation = EventTypeEvaluation.builder()
                                                                     .type(eventTypeEvaluationForm.getType())
                                                                     .startValue(eventTypeEvaluationForm.getStartValue())
                                                                     .endValue(eventTypeEvaluationForm.getEndValue())
                                                                     .finalValue(eventTypeEvaluationForm.getFinalValue())
                                                                     .build();
        Event event = this.repository.findOne(eventTypeEvaluationForm.getEventId());

        this.eventTypeEvaluationRepository.save(eventTypeEvaluation);
        event.getEventTypeEvaluations().add(eventTypeEvaluation);
        this.repository.save(event);

    }


    @Override
    public void addFileOfService(Event event, MultipartFile multipartFile) {
        storageService.saveFile(event, multipartFile);
    }

    @Override
    public List<Event> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
