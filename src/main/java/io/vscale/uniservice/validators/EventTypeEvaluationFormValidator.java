package io.vscale.uniservice.validators;

import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.EventTypeEvaluation;
import io.vscale.uniservice.forms.general.EventTypeEvaluationForm;
import io.vscale.uniservice.repositories.data.EventRepository;
import io.vscale.uniservice.repositories.data.EventTypeEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 29.04.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Component
public class EventTypeEvaluationFormValidator implements Validator{

    private final EventRepository eventRepository;
    private final EventTypeEvaluationRepository eventTypeEvaluationRepository;

    @Autowired
    public EventTypeEvaluationFormValidator(EventRepository eventRepository,
                                            EventTypeEvaluationRepository eventTypeEvaluationRepository) {
        this.eventRepository = eventRepository;
        this.eventTypeEvaluationRepository = eventTypeEvaluationRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.getName().equals(EventTypeEvaluationForm.class.getName());
    }

    @Override
    public void validate(Object target, Errors errors) {

        EventTypeEvaluationForm form = (EventTypeEvaluationForm) target;

        Event event = this.eventRepository.findOne(form.getEventId());

        if(event == null){
            errors.reject("bad.eventId", "Такого мероприятия не существует");
        }

        EventTypeEvaluation checkedEventTypeEvaluation = this.eventTypeEvaluationRepository.findByType(form.getType());

        if(checkedEventTypeEvaluation == null){
            errors.reject("bad.evaluationId", "Нет такого типа оценивания");
        }

        Field[] fields = EventTypeEvaluationForm.class.getDeclaredFields();

        List<String> fieldsNames = Arrays.stream(fields)
                                         .map(Field::getName)
                                         .collect(Collectors.toList());

        List<String> errorTypes = new ArrayList<>();
        List<String> errorDescriptions = new ArrayList<>();

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        fieldsNames.forEach(fieldName ->{
            sb1.append("empty.").append(fieldName);
            errorTypes.add(sb1.toString());
            sb1.setLength(0);
        });

        fieldsNames.forEach(fieldName ->{
            sb2.append("Не заполнено поле для ").append(fieldName);
            errorDescriptions.add(sb2.toString());
            sb2.setLength(0);
        });

        AtomicInteger counter = new AtomicInteger();
        counter.set(0);

        fieldsNames.forEach(fieldName ->{
            int i = counter.getAndIncrement();
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldName, errorTypes.get(i), errorDescriptions.get(i));
        });

    }
}
