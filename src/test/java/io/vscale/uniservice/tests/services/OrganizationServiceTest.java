package io.vscale.uniservice.tests.services;

import io.vscale.uniservice.application.Application;
import io.vscale.uniservice.domain.Organization;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.services.interfaces.events.OrganizationService;
import io.vscale.uniservice.services.interfaces.student.StudentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * 01.05.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class OrganizationServiceTest {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private StudentService studentService;

    @Test
    public void testGetNumberOfPeople(){
        Student student1 = Student.builder()
                                  .gender("M")
                                  .build();

        Student student2 = Student.builder()
                                  .gender("Ж")
                                  .build();

        Set<Student> studentSet = new HashSet<>();
        studentSet.add(student1);
        studentSet.add(student2);

        Organization organization = Organization.builder()
                                                .title("MusicOrganization")
                                                .students(studentSet)
                                                .build();

        organizationService.save(organization);

        Integer found = organizationService.getNumberOfPeople(organization.getId());

        Assert.assertEquals(2, found.intValue());


    }

    @Test
    public void testParticipants(){
        Student student3 = Student.builder()
                                  .gender("M")
                                  .build();

        Student student4 = Student.builder()
                                  .gender("Ж")
                                  .build();

        Set<Student> studentSet = new HashSet<>();
        studentSet.add(student3);
        studentSet.add(student4);

        Organization organization = Organization.builder()
                                                .title("SportOrganization")
                                                .students(studentSet)
                                                .build();

        organizationService.save(organization);

        Set<Student> students = organizationService.getParticipants(organization.getId());
        Assert.assertTrue(students.contains(student3));
        Assert.assertTrue(students.contains(student4));


    }

}
