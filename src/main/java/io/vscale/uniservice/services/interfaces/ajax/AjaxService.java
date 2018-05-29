package io.vscale.uniservice.services.interfaces.ajax;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

/**
 * @author Dilyara Gabdreeva
 * 11-602
 * 27.05.2018
 */


public interface AjaxService {
    List<String> getGroupsByCourse(String course, String degree);
    List<String> getCourseByDegree(String degree);
    Set<String> getAllOrganizations();
}
