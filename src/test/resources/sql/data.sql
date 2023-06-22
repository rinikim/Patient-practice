INSERT INTO Hospital (created_at, name, care_facility_number, director_name, deleted)
VALUES
    (CURRENT_TIMESTAMP(),'사랑병원', '230620-23db3b2f995b', '김병원장', false),
    (CURRENT_TIMESTAMP(),'더큰마음병원', '230620-1d1f637d4d70', '이병원장', false),
    (CURRENT_TIMESTAMP(),'코미코병원', '230620-7848f80f1d40', '박병원장', false);

INSERT INTO CODE_GROUP (code_group, name, description)
VALUES
    ('성별코드', '성별코드', '성별을 표시'),
    ('방문상태코드', '방문상태코드', '환자방문의 상태'),
    ('진료과목코드', '진료과목코드', '진료과목'),
    ('진료유형코드', '진료유형코드', '진료의 유형');

INSERT INTO CODE (code_group, code, name)
VALUES
    ('성별코드', 'M', '남'),
    ('성별코드', 'F', '여'),
    ('방문상태코드', '1', '방문중'),
    ('방문상태코드', '2', '종료'),
    ('방문상태코드', '3', '취소'),
    ('진료과목코드', '01', '내과'),
    ('진료과목코드', '02', '안과'),
    ('진료유형코드', 'D', '약처방'),
    ('진료유형코드', 'T', '검사');