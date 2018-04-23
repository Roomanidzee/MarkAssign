package io.vscale.uniservice.services.interfaces.admin;

import io.vscale.uniservice.forms.rest.CooperatorRESTForm;

/**
 * 17.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface CooperatorAdminService {
    void makeRESTCooperator(CooperatorRESTForm cooperatorRESTForm);
}
