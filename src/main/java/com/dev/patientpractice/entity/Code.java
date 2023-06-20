package com.dev.patientpractice.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Code {

    @Id
    @Column(nullable = false, length = 10)
    private String code;  // 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_group", nullable = false)
    private CodeGroup codeGroup;  // 코드그룹


    @Column(nullable = false, length = 10)
    private String name;  // 코드명
}
