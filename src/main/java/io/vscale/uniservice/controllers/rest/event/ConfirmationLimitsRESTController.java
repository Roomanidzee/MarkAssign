package io.vscale.uniservice.controllers.rest.event;

import io.vscale.uniservice.domain.ConfirmationLimits;
import io.vscale.uniservice.services.interfaces.events.ConfirmationLimitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 22.04.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@RestController
@RequestMapping({"/api_v1/admin_role", "/api_v1/student_role"})
public class ConfirmationLimitsRESTController {

    private final ConfirmationLimitsService confirmationLimitsService;

    @Autowired
    public ConfirmationLimitsRESTController(ConfirmationLimitsService confirmationLimitsService) {
        this.confirmationLimitsService = confirmationLimitsService;
    }

    @GetMapping("/get_subjects/{number}")
    public ResponseEntity<List<ConfirmationLimits>> getSubjects(@PathVariable("number") Long semesterNumber){
        return ResponseEntity.ok(this.confirmationLimitsService.findAllBySemesterNumber(semesterNumber));
    }

}
