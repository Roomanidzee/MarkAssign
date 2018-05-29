package io.vscale.uniservice.controllers.rest.ajax;

import io.vscale.uniservice.dto.Filter;
import io.vscale.uniservice.services.interfaces.ajax.AjaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Dilyara Gabdreeva
 * 11-602
 * 27.05.2018
 */

@RestController
public class AjaxController {

    @Autowired
    private AjaxService ajaxService;

    @PostMapping("/get/groups")
    public ResponseEntity getGroups(@RequestBody Filter filter) {
        String degree = filter.getDegree();
        String course = filter.getCourse();
        List<String> groups = ajaxService.getGroupsByCourse(course, degree);
        return ResponseEntity.ok(groups);
    }
    @PostMapping("/get/courses")
    public ResponseEntity getCourses(@RequestBody String degree) {
        degree = degree.substring(1, 2);
        List<String> courses = ajaxService.getCourseByDegree(degree);
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/get/organizations")
    public ResponseEntity getOrganizations() {
        return ResponseEntity.ok(ajaxService.getAllOrganizations());
    }

}
