package io.vscale.uniservice.controllers.rest.admin;

import io.vscale.uniservice.domain.Profile;
import io.vscale.uniservice.forms.rest.ProfileForm;
import io.vscale.uniservice.services.interfaces.admin.ProfileAdminService;
import io.vscale.uniservice.services.interfaces.user.ProfileService;
import io.vscale.uniservice.validators.ProfileFormValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProfileRESTController {

    private ProfileFormValidator profileFormValidator;
    private ProfileService profileService;
    private ProfileAdminService profileAdminService;

    @InitBinder
    public void initUserFormValidator(WebDataBinder binder){
        binder.addValidators(this.profileFormValidator);
    }

    @PostMapping("/attache_profile")
    public List<Profile> attachProfile(@RequestBody @Valid @ModelAttribute("profileForm")ProfileForm profileForm){
        this.profileAdminService.addProfileToUser(profileForm);
        return this.profileService.getAllProfiles();
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<Profile>> getAllProfiles(){
        return ResponseEntity.ok(this.profileService.getAllProfiles());
    }

}
