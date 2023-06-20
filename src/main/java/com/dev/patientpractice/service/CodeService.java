package com.dev.patientpractice.service;

import com.dev.patientpractice.exception.ErrorCode;
import com.dev.patientpractice.exception.PatientApplicationException;
import com.dev.patientpractice.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;

    public void checkCode(String codeGroup, String code) {
        codeRepository.findByCodeGroup_CodeGroupAndCode(codeGroup, code)
                .orElseThrow(() -> new PatientApplicationException(ErrorCode.CODE_NOT_FOUND, String.format("%s: %s", codeGroup, code)));
    }
}
