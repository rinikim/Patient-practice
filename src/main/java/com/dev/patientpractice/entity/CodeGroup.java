package com.dev.patientpractice.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CodeGroup {

    @Id
    @Column(name = "code_group", nullable = false, length = 10)
    private String codeGroup;  // 코드그룹

    @OneToMany(mappedBy = "codeGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Code> codes = new ArrayList<>();  // 코드

    @Column(nullable = false, length = 10)
    private String name;  // 코드그룹명

    @Column(nullable = false, length = 10)
    private String description;  // 설명
}
