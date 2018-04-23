package io.vscale.uniservice.services.implementations.admin;

import io.vscale.uniservice.domain.Group;
import io.vscale.uniservice.domain.Profile;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.forms.rest.StudentRESTForm;
import io.vscale.uniservice.repositories.data.GroupRepository;
import io.vscale.uniservice.repositories.data.ProfileRepository;
import io.vscale.uniservice.repositories.data.StudentRepository;
import io.vscale.uniservice.repositories.indexing.StudentESRepository;
import io.vscale.uniservice.services.interfaces.admin.StudentAdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 17.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StudentAdminServiceImpl implements StudentAdminService{

    private ProfileRepository profileRepository;
    private StudentRepository studentRepository;
    private StudentESRepository studentESRepository;
    private GroupRepository groupRepository;

    @Override
    public void makeRESTStudent(StudentRESTForm studentRESTForm) {

        Profile profile = this.profileRepository.findOne(studentRESTForm.getProfileId());

        Group group = this.groupRepository.findByTitle(studentRESTForm.getGroupTitle())
                                          .orElseThrow(IllegalArgumentException::new);

        Student student = Student.builder()
                                 .profile(profile)
                                 .gender(studentRESTForm.getGender())
                                 .course(studentRESTForm.getCourse())
                                 .groups(Collections.singleton(group))
                                 .build();

        this.studentRepository.save(student);
        this.studentESRepository.save(student);

    }
}
