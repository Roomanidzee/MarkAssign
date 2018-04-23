package io.vscale.uniservice.tests.rest.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vscale.uniservice.controllers.rest.admin.StudentAdminRESTController;
import io.vscale.uniservice.domain.Student;
import io.vscale.uniservice.forms.rest.StudentRESTForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

/**
 * 18.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = {StudentAdminRESTController.class})
@ActiveProfiles("test")
public class StudentAdminRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentAdminRESTController studentAdminRESTController;

    @Test
    public void shouldCreateNewStudent() throws Exception{

        StudentRESTForm studentRESTForm = StudentRESTForm.builder()
                                             .course((byte) 1)
                                             .gender("Мужской")
                                             .build();

        Student student = Student.builder()
                                 .course(studentRESTForm.getCourse())
                                 .gender(studentRESTForm.getGender())
                                 .build();

        BDDMockito.given(this.studentAdminRESTController.createStudent(studentRESTForm))
                  .willReturn(Collections.singletonList(student));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInString = objectMapper.writeValueAsString(studentRESTForm);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api_v1/admin_role/create_student")
                                      .with(mockHttpServletRequest -> {
                                          mockHttpServletRequest.setContentType("application/json");
                                          mockHttpServletRequest.setContent(jsonInString.getBytes());
                                          return mockHttpServletRequest;
                                        }
                                      )
                                      .accept(MediaType.APPLICATION_JSON)
                                      .contentType(MediaType.APPLICATION_JSON))
                                      .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

}
