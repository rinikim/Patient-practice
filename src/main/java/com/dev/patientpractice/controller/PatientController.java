package com.dev.patientpractice.controller;

import com.dev.patientpractice.dto.request.PatientRegistrationRequest;
import com.dev.patientpractice.dto.response.Response;
import com.dev.patientpractice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/v1/patients")
@RequiredArgsConstructor
@RestController
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public Response generatePatient(@RequestBody @Valid PatientRegistrationRequest body) {
        patientService.generatePatient(body);
        return Response.success(null);
    }
}

