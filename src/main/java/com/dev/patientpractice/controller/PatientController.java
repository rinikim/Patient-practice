package com.dev.patientpractice.controller;

import com.dev.patientpractice.dto.request.patient.PatientModificationRequest;
import com.dev.patientpractice.dto.request.patient.PatientRegistrationRequest;
import com.dev.patientpractice.dto.request.patient.PatientsInquiryRequest;
import com.dev.patientpractice.dto.response.Response;
import com.dev.patientpractice.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping
    public Response getPatients(@Valid PatientsInquiryRequest params) {
        return Response.success(patientService.getPatients(params));
    }

    @GetMapping("/{patientId}")
    public Response getPatient(@PathVariable Long patientId) {
        return Response.success(patientService.getPatient(patientId));
    }

    @PatchMapping("/{patientId}")
    public Response updatePatient(@PathVariable Long patientId,
                                  @RequestBody @Valid PatientModificationRequest body) {
        patientService.updatePatient(patientId, body);
        return Response.success(null);
    }

    @DeleteMapping("/{patientId}")
    public Response deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return Response.success(null);
    }
}

