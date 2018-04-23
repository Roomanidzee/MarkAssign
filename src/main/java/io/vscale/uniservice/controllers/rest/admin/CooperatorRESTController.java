package io.vscale.uniservice.controllers.rest.admin;

import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.forms.general.CooperatorForm;
import io.vscale.uniservice.forms.rest.CooperatorRESTForm;
import io.vscale.uniservice.services.interfaces.admin.CooperatorAdminService;
import io.vscale.uniservice.services.interfaces.cooperator.CooperatorService;
import io.vscale.uniservice.validators.CooperatorFormValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 17.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@RestController
@RequestMapping("/api_v1/admin_role")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CooperatorRESTController {

    private CooperatorFormValidator cooperatorFormValidator;
    private CooperatorAdminService cooperatorAdminService;
    private CooperatorService cooperatorService;

    @InitBinder
    public void initUserFormValidator(WebDataBinder binder){
        binder.addValidators(this.cooperatorFormValidator);
    }

    @PostMapping("/create_cooperator")
    public List<Cooperator> createCooperator(
            @RequestBody @Valid @ModelAttribute("cooperatorForm") CooperatorRESTForm cooperatorForm){

        this.cooperatorAdminService.makeRESTCooperator(cooperatorForm);
        return this.cooperatorService.getAllCooperators();

    }

    @PostMapping("/update_cooperator")
    public ResponseEntity<List<Cooperator>> updateCooperator(
            @RequestBody @Valid @ModelAttribute("cooperatorForm") CooperatorForm cooperatorForm){

        this.cooperatorService.updateCooperator(cooperatorForm);
        return ResponseEntity.ok(this.cooperatorService.getAllCooperators());

    }

    @PostMapping("/delete_cooperator")
    public ResponseEntity<List<Cooperator>> deleteCooperator(
            @RequestBody @Valid @ModelAttribute("cooperatorForm") CooperatorForm cooperatorForm){

        this.cooperatorService.deleteCooperator(cooperatorForm);
        return ResponseEntity.ok(this.cooperatorService.getAllCooperators());

    }

    @GetMapping("/cooperators/asc")
    public ResponseEntity<Page<Cooperator>> getCooperatorsAsc(Pageable pageable){
        return ResponseEntity.ok(this.cooperatorService.retrieveAllCooperatorsAsc(pageable));
    }

    @GetMapping("/cooperators/desc")
    public ResponseEntity<Page<Cooperator>> getCooperatorsDesc(Pageable pageable){
        return ResponseEntity.ok(this.cooperatorService.retrieveAllCooperatorsDesc(pageable));
    }


}
