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
public class Hospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<Patient> patients = new ArrayList<>();  // 환자

    @Column(nullable = false, length = 45)
    private String name;  // 병원명

    @Column(nullable = false, length = 20)
    private String careFacilityNumber;  // 요양기관번호

    @Column(nullable = false, length = 10)
    private String directorName;  // 병원장명

    @Column(nullable = false)
    private boolean deleted = false;  // 삭제 여부

}
