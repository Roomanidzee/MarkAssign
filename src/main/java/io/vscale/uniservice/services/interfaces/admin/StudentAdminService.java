package io.vscale.uniservice.services.interfaces.admin;

import io.vscale.uniservice.forms.rest.StudentRESTForm;

/**
 * 17.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface StudentAdminService {
    void makeRESTStudent(StudentRESTForm studentRESTForm);
}
