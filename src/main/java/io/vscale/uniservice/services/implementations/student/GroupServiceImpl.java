package io.vscale.uniservice.services.implementations.student;

import io.vscale.uniservice.domain.Confirmation;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Group;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.repositories.data.GroupRepository;
import io.vscale.uniservice.services.interfaces.student.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 11.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
public class GroupServiceImpl implements GroupService{

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> getAllGroups() {
        return this.groupRepository.findAll();
    }

    @Override
    public Map<Student, List<FileOfService>> getStudentsFiles(Long groupID) {

        Group group = this.groupRepository.findOne(groupID);
        Set<Student> students = group.getStudents();

        Map<Student, List<FileOfService>> resultMap =
                students.stream()
                        .collect(Collectors.toMap(student -> student, student -> null, (a, b) -> b));

        List<FileOfService> tempFileList = new ArrayList<>();
        Set<Confirmation> tempConfirmations = new HashSet<>();

        resultMap.forEach((student, value) -> {

            tempConfirmations.addAll(student.getConfirmations());

            tempConfirmations.stream()
                             .map(Confirmation::getFileOfService)
                             .filter(Objects::nonNull)
                             .forEach(tempFileList::add);

            tempFileList.add(student.getProfile().getAvatar());

            resultMap.put(student, tempFileList);

            tempConfirmations.clear();
            tempFileList.clear();

        });

        return resultMap;

    }
}
