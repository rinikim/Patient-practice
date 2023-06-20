package com.dev.patientpractice.controller;

import com.dev.patientpractice.dto.request.PatientModificationRequest;
import com.dev.patientpractice.dto.request.PatientRegistrationRequest;
import com.dev.patientpractice.dto.response.Response;
import com.dev.patientpractice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{patientId}")
    public Response updatePatient(@PathVariable Long patientId,
                                  @RequestBody @Valid PatientModificationRequest body) {
        patientService.updatePatient(patientId, body);
        return Response.success(null);
    }
}

