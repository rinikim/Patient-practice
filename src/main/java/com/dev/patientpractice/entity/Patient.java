package com.dev.patientpractice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;  // 병원

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();  // 환자방문

    @Column(nullable = false, length = 45)
    private String name;  // 환자명

    @Column(nullable = false, length = 13)
    private String registrationNumber;  // 환자등록번호

    @Column(nullable = false, length = 10)
    private String genderCode;  // 성별코드

    @Column(length = 10)
    private String birthDate;  // 생년월일

    @Column(length = 20)
    private String phoneNumber;  // 휴대전화번호

    @Builder
    public Patient(Hospital hospital, String name, String registrationNumber, String genderCode, String birthDate, String phoneNumber) {
        this.hospital = hospital;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
