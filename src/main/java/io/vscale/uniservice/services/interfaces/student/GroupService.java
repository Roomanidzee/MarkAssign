package io.vscale.uniservice.services.interfaces.student;

import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Group;
import io.vscale.uniservice.domain.Student;

import java.util.List;
import java.util.Map;

/**
 * 11.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface GroupService {

    List<Group> getAllGroups();
    Map<Student, List<FileOfService>> getStudentsFiles(Long groupID);

}
