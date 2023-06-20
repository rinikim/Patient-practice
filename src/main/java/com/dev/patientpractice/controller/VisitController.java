package com.dev.patientpractice.controller;

import com.dev.patientpractice.dto.request.VisitCreationRequest;
import com.dev.patientpractice.dto.response.Response;
import com.dev.patientpractice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/v1/visits")
@RestController
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public Response createVisit(@RequestBody @Valid VisitCreationRequest body) {
        visitService.createVisit(body);
        return Response.success(null);
    }
}
