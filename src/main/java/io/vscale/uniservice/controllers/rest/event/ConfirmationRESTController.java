package io.vscale.uniservice.controllers.rest.event;

import io.vscale.uniservice.domain.Confirmation;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Profile;
import io.vscale.uniservice.services.interfaces.events.ConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 23.04.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestController
@RequestMapping("/api_v1/confirmations")
public class ConfirmationRESTController {

    private final ConfirmationService confirmationService;

    @Autowired
    public ConfirmationRESTController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @GetMapping("/find_by_file")
    public ResponseEntity<List<Confirmation>> getConfirmationsByFile(FileOfService file){
        return ResponseEntity.ok(this.confirmationService.findAllByFile(file));
    }

    @GetMapping("/find_by_profile")
    public ResponseEntity<List<Confirmation>> getConfirmationsByProfile(Profile profile){
        return ResponseEntity.ok(this.confirmationService.findAllByProfile(profile));
    }
}
