package io.vscale.uniservice.tests.rest.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vscale.uniservice.controllers.rest.admin.CooperatorRESTController;
import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.forms.rest.CooperatorRESTForm;
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
@WebMvcTest(value = {CooperatorRESTController.class})
@ActiveProfiles("test")
public class CooperatorRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CooperatorRESTController cooperatorRESTController;

    @Test
    public void shouldCreateNewCooperator() throws Exception {

        CooperatorRESTForm cooperatorRESTForm = CooperatorRESTForm.builder()
                                                      .profileId((long) 2)
                                                      .appointment("Должность1")
                                                      .recordOfService((byte) 5)
                                                      .build();

        Cooperator cooperator = Cooperator.builder()
                                          .appointment(cooperatorRESTForm.getAppointment())
                                          .recordOfService(cooperatorRESTForm.getRecordOfService())
                                          .build();

        BDDMockito.given(this.cooperatorRESTController.createCooperator(cooperatorRESTForm))
                  .willReturn(Collections.singletonList(cooperator));

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonInString = objectMapper.writeValueAsString(cooperatorRESTForm);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api_v1/admin_role/create_cooperator")
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
