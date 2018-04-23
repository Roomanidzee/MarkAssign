package io.vscale.uniservice.controllers.rest.admin;

import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.dto.TokenDTO;
import io.vscale.uniservice.forms.rest.NewUserForm;
import lombok.AllArgsConstructor;
import io.vscale.uniservice.services.interfaces.admin.UserAdminService;
import io.vscale.uniservice.services.interfaces.user.UserService;
import io.vscale.uniservice.validators.NewUserFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
public class UserRESTController {

    private UserAdminService userAdminService;
    private UserService userService;
    private NewUserFormValidator newUserFormValidator;

    @InitBinder
    public void initUserFormValidator(WebDataBinder binder){
        binder.addValidators(this.newUserFormValidator);
    }

    @PostMapping("/add_user")
    public User addNewUser(@RequestBody @Valid @ModelAttribute("newUserForm") NewUserForm newUserForm){
        this.userAdminService.addNewUserREST(newUserForm);
        return this.userService.getUserByLogin(newUserForm.getLogin());
    }

    @GetMapping("/get_user/{user_id}")
    public User getUser(@PathVariable("user_id") Long userId){
        return this.userService.getUserById(userId);
    }

    @GetMapping("/get_all_users")
    public List<User> getAllUsers(){
        return this.userService.getAllUsers();
    }

    @PostMapping("/user/retrieve_token/{id}")
    public ResponseEntity<TokenDTO> giveTokenToUser(@PathVariable("id") Long id){
        return new ResponseEntity<>(this.userAdminService.retrieveTokenToUser(id), HttpStatus.OK);
    }

}
