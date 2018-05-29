package io.vscale.uniservice.services.implementations.ajax;

import io.vscale.uniservice.domain.EventTypeEvaluation;
import io.vscale.uniservice.domain.Group;
import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.repositories.data.EventTypeEvaluationRepository;
import io.vscale.uniservice.repositories.data.GroupRepository;
import io.vscale.uniservice.repositories.data.OrganizationRepository;
import io.vscale.uniservice.services.interfaces.ajax.AjaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

/**
 * @author Dilyara Gabdreeva
 * 11-602
 * 27.05.2018
 */

@Service
public class AjaxServiceImpl implements AjaxService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EventTypeEvaluationRepository eventTypeEvaluationRepository;

    @Override
    public List<String> getGroupsByCourse(String c, String d) {
        int course = Integer.parseInt(c);
        int degree = Integer.parseInt(d);
        List<String> groups = new ArrayList<>(Arrays.asList("Не выбрано"));
        if (degree == 1) {

            String group = "";
            switch (course) {
                case 1:
                    group = "11-70";
                    break;
                case 2:
                    group = "11-60";
                    break;
                case 3:
                    group = "11-50";
                    break;
                case 4:
                    group = "11-4";
                    break;
            }
            List<Group> allGroups = groupRepository.findAll();
            for (Group g : allGroups) {
                if (g.getTitle().startsWith(group)) {
                    groups.add(g.getTitle());
                }
            }

        } else {
            String group = "";
            switch (course) {
                case 1:
                    group = "11-71";
                    break;
                case 2:
                    group = "11-61";
                    break;
            }
            List<Group> allGroups = groupRepository.findAll();
            for (Group g : allGroups) {
                if (g.getTitle().startsWith(group)) {
                    groups.add(g.getTitle());
                }
            }

        }
        return groups;
        //вернуть список групп по номеру курса, в course лежит переменная 1,
        // что означает 1 курс (группы 11-701 и тд), 2 - второй курс и тд
    }

    @Override
    public List<String> getCourseByDegree(String d) {
        int degree = Integer.parseInt(d);
        List<String> courses = new ArrayList<>(Arrays.asList("Не выбрано"));
        switch (degree) {
            case 1: courses.addAll(Arrays.asList("1 курс", "2 курс", "3 курс", "4 курс")); break;
            case 2: courses.addAll(Arrays.asList("1 курс", "2 курс")); break;
        }
        return courses;
    }

    @Override
    public Set<String> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        Set<String> titles = new LinkedHashSet<>();
        titles.add("Не выбрано");
        for (Organization organization : organizations) {
            titles.add(organization.getTitle());
        }
        return titles;
    }

}
