package io.vscale.uniservice.services.interfaces.admin;

import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.forms.rest.CooperatorRESTForm;
import org.springframework.data.domain.Page;


/**
 * 17.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface CooperatorAdminService {
    void makeRESTCooperator(CooperatorRESTForm cooperatorRESTForm);
    Page<Cooperator> searchBySurname(String surname);
}
