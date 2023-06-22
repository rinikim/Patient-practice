package com.dev.patientpractice.controller;

import com.dev.patientpractice.dto.request.patient.PatientModificationRequest;
import com.dev.patientpractice.dto.request.patient.PatientRegistrationRequest;
import com.dev.patientpractice.dto.request.visit.VisitCreationRequest;
import com.dev.patientpractice.service.PatientService;
import com.dev.patientpractice.service.VisitService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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

    @Autowired
    private VisitService visitService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        beforeCreate();
    }

    public void beforeCreate() {
        patientService.generatePatient(createPatientFixture());
        visitService.createVisit(createVisitFixture());
    }

    @Order(1)
    @DisplayName("환자조회")
    @Test
    public void getPatient() throws Exception {
        Long patientId = 1L;

        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/patients/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-patient",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과"),
                                fieldWithPath("result.patientId").type(JsonFieldType.NUMBER).description("환자 ID"),
                                fieldWithPath("result.name").type(JsonFieldType.STRING).description("환자명"),
                                fieldWithPath("result.registrationNumber").type(JsonFieldType.STRING).description("환자등록번호"),
                                fieldWithPath("result.genderCode").type(JsonFieldType.STRING).description("성별코드"),
                                fieldWithPath("result.birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("result.phoneNumber").type(JsonFieldType.STRING).description("휴대전화번호"),
                                fieldWithPath("result.hospital.id").type(JsonFieldType.NUMBER).description("병원 ID"),
                                fieldWithPath("result.hospital.name").type(JsonFieldType.STRING).description("병원명"),
                                fieldWithPath("result.hospital.careFacilityNumber").type(JsonFieldType.STRING).description("요양기관번호"),
                                fieldWithPath("result.hospital.directorName").type(JsonFieldType.STRING).description("병원장명"),
                                fieldWithPath("result.visits[].id").type(JsonFieldType.NUMBER).description("환자방문 ID"),
                                fieldWithPath("result.visits[].receivedAt").type(JsonFieldType.STRING).description("접수일시"),
                                fieldWithPath("result.visits[].visitStatusCode").type(JsonFieldType.STRING).description("방문상태코드")
                        )
                ));
    }

    @Order(2)
    @DisplayName("환자목록조회")
    @Test
    public void getPatients() throws Exception {
        Long patientId = 1L;

        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("pageNo", "1")
                        .queryParam("pageSize", "10")
                        .queryParam("hospitalId", "1")
                        .queryParam("name", "홍길동")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-patients",
                        requestParameters(
                                parameterWithName("pageNo").optional().description("페이지 번호 (기본값 : 1)"),
                                parameterWithName("pageSize").optional().description("페이지 사이즈 (기본값 : 10)"),
                                parameterWithName("hospitalId").optional().description("병원 ID"),
                                parameterWithName("name").optional().description("환자명"),
                                parameterWithName("registrationNumber").optional().description("환자등록번호"),
                                parameterWithName("birthDate").optional().description("생년월일")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과"),
                                fieldWithPath("result.patients[].name").type(JsonFieldType.STRING).description("환자명"),
                                fieldWithPath("result.patients[].registrationNumber").type(JsonFieldType.STRING).description("환자등록번호"),
                                fieldWithPath("result.patients[].genderCode").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("result.patients[].birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("result.patients[].phoneNumber").type(JsonFieldType.STRING).description("휴대전화번호"),
                                fieldWithPath("result.patients[].latestVisit").optional().type(JsonFieldType.STRING).description("최근방문"),
                                fieldWithPath("result.page.page").type(JsonFieldType.NUMBER).description("현제 페이지"),
                                fieldWithPath("result.page.size").type(JsonFieldType.NUMBER).description("현재 페이지에서 보이는 데이터 수"),
                                fieldWithPath("result.page.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("result.page.totalCount").type(JsonFieldType.NUMBER).description("전체 데이터 수")
                        )
                ));
    }

    @Order(3)
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
                                fieldWithPath("birthDate").optional().type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("phoneNumber").optional().type(JsonFieldType.STRING).description("휴대전화번호")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과")
                        )
                ));
    }

    @Order(4)
    @DisplayName("환자수정")
    @Test
    public void updatePatient() throws Exception {
        PatientModificationRequest body = updatePatientFixture();
        Long patientId = 1L;

        mockMvc.perform(patch("/v1/patients/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("patch-patient",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").optional().type(JsonFieldType.STRING).description("환자명"),
                                fieldWithPath("genderCode").optional().type(JsonFieldType.STRING).description("성별코드"),
                                fieldWithPath("birthDate").optional().type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("phoneNumber").optional().type(JsonFieldType.STRING).description("휴대전화번호")
                        ),
                        responseFields(
                                fieldWithPath("resultCode").type(JsonFieldType.STRING).description("결과")
                        )
                ));
    }

    @Order(5)
    @DisplayName("환자삭제")
    @Test
    public void deletePatient() throws Exception {
        Long patientId = 1L;

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/v1/patients/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-patient",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
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
                "010-1111-1111"
        );
    }

    public VisitCreationRequest createVisitFixture() {
        return new VisitCreationRequest(
                1L,
                1L,
                LocalDateTime.now(),
                "1"
        );
    }
}
