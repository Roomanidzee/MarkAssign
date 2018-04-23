package io.vscale.uniservice.services.interfaces.admin;

import io.vscale.uniservice.dto.TokenDTO;
import io.vscale.uniservice.forms.rest.NewUserForm;


/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface UserAdminService {

    void addNewUserREST(NewUserForm newUserForm);
    TokenDTO retrieveTokenToUser(Long userId);

}
