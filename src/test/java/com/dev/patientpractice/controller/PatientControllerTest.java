package com.dev.patientpractice.controller;

import com.dev.patientpractice.dto.request.patient.PatientModificationRequest;
import com.dev.patientpractice.dto.request.patient.PatientRegistrationRequest;
import com.dev.patientpractice.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientService patientService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        createPatient();
    }

    public void createPatient() {
        patientService.generatePatient(createPatientFixture());
    }

    @DisplayName("환자생성")
    @Test
    public void generatePatient() throws Exception {
        PatientRegistrationRequest body = createPatientFixture();
        mockMvc.perform(post("/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-patient",
                        requestFields(
                                fieldWithPath("hospitalId").type(JsonFieldType.NUMBER).description("병원 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("환자명"),
                                fieldWithPath("genderCode").type(JsonFieldType.STRING).description("성별코드"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화번호")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과")
                        )
                ));
    }

    @DisplayName("환자수정")
    @Test
    public void updatePatient() throws Exception {
        PatientModificationRequest body = updatePatientFixture();

        mockMvc.perform(patch("/v1/patients/{patientId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("patch-patient",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("환자명"),
                                fieldWithPath("genderCode").type(JsonFieldType.STRING).description("성별코드"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화번호")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과")
                        )
                ));
    }

    public PatientModificationRequest updatePatientFixture() {
        return new PatientModificationRequest(
                "홍길동",
                "M",
                LocalDate.of(1990, 1, 1),
                "010-1111-0110"
        );
    }


    public PatientRegistrationRequest createPatientFixture() {
        return new PatientRegistrationRequest(
                1L,
                "홍길동",
                "M",
                LocalDate.of(1990, 1, 1),
                "010-1111-0110"
        );
    }
}
