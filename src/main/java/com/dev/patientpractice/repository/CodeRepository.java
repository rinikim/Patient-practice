package com.dev.patientpractice.repository;

import com.dev.patientpractice.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findByCodeGroup_CodeGroupAndCode(String codeGroup, String code);
}
