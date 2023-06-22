# 환자 API 명세

🔗 깃허브에 프로젝트와 이슈를 작성하였습니다.

url : https://github.com/users/rinikim/projects/2/views/1

💻 H2 DB 접속 url 입니다.

- 서버 실행 시 병원, 코드그룹, 코드 테이블의 초기 데이터가 생성됩니다.

url : http://localhost:8080/h2-console


# 환자 API

---

## 환자 등록 API

### 기본 정보
```jsx
POST /v1/patients HTTP/1.1
Host: http://localhost:8080/v1/patients
```

### HTTP Request

**Example**
```java
{
    "hospitalId" : 1,
    "name" : "김혜린",
    "genderCode" : "F",
    "birthDate" : "1994-04-25",
    "phoneNumber" : "010-1234-5678"
}
```
<table>
    <tr>
      <td>Name</td>
      <td>Type</td>
      <td>Description</td>
      <td>Required</td>
    </tr>
    <tr>
      <td>hospitalId</td>
      <td>Number</td>
      <td>병원 ID</td>
      <td>O</td>
    </tr>
    <tr>
      <td>name</td>
      <td>String</td>
      <td>환자명 </td>
      <td>O</td>
    </tr>
    <tr>
      <td>genderCode</td>
      <td>String</td>
      <td>성별코드 </br>
        M : 남 </br>
        F : 여 </td>
      <td>O</td>
    </tr>
    <tr>
      <td>birthDate</td>
      <td>String</td>
      <td>생년월일 </br>
        'yyyy-MM-dd'으로 입력</td>
      <td>X</td>
    </tr>
    <tr>
      <td>phoneNumber</td>
      <td>String</td>
      <td>휴대전화번호</td>
      <td>X</td>
    </tr>
  </table>

### HTTP Response

**Example**
```java
{
  "resultCode": "SUCCESS"
}
```

| Name | Type | Description |
| --- | --- | --- |
| resultCode | String | 결과 코드 |

---

## 환자 수정 API


### 기본 정보
```jsx
PATCH /v1/patients/{patientId} HTTP/1.1
Host: http://localhost:8080/v1/patients/1
```

### HTTP Request

**Example**
```java
{
    "name" : "김혜린",
    "genderCode" : "F",
    "birthDate" : "1994-04-25",
    "phoneNumber" : "010-1234-5678"
}
```
<table>
    <tr>
      <td>Name</td>
      <td>Type</td>
      <td>Description</td>
      <td>Required</td>
    </tr>
    <tr>
      <td>name</td>
      <td>String</td>
      <td>환자명 </td>
      <td>X</td>
    </tr>
    <tr>
      <td>genderCode</td>
      <td>String</td>
      <td>성별코드 </br>
        M : 남 </br>
        F : 여 </td>
      <td>X</td>
    </tr>
    <tr>
      <td>birthDate</td>
      <td>String</td>
      <td>생년월일 </br>
        'yyyy-MM-dd'으로 입력</td>
      <td>X</td>
    </tr>
    <tr>
      <td>phoneNumber</td>
      <td>String</td>
      <td>휴대전화번호</td>
      <td>X</td>
    </tr>
  </table>

### HTTP Response

**Example**
```java
{
  "resultCode": "SUCCESS"
}
```

| Name | Type | Description |
| --- | --- | --- |
| resultCode | String | 결과 코드 |

---


## 환자 삭제 API


### 기본 정보
```jsx
DELETE /v1/patients/{patientId} HTTP/1.1
Host: http://localhost:8080/v1/patients/1
```

### HTTP Response

**Example**
```java
{
  "resultCode": "SUCCESS"
}
```

| Name | Type | Description |
| --- | --- | --- |
| resultCode | String | 결과 코드 |

---


## 환자 목록 조회 API


### 기본 정보
```jsx
GET /v1/patients/{patientId} HTTP/1.1
Host: http://localhost:8080/v1/patients?pageNo=1&pageSize=10&hospitalId=1&name=김혜린&registrationNumber=202300085&genderCode=F&birthDate=1994-04-25&phoneNumber=010-1234-5678
```

### HTTP Request

**Example**

<table>
    <tr>
      <td>Name</td>
      <td>Type</td>
      <td>Description</td>
      <td>Required</td>
    </tr>
    <tr>
      <td>pageNo</td>
      <td>Number</td>
      <td>페이지 번호</td>
      <td>X</td>
    </tr>
    <tr>
      <td>pageSize</td>
      <td>Number</td>
      <td>페이지 사이즈</td>
      <td>X</td>
    </tr>
    <tr>
      <td>hospitalId</td>
      <td>Number</td>
      <td>병원 ID</td>
      <td>O</td>
    </tr>
    <tr>
      <td>birthDate</td>
      <td>String</td>
      <td>생년월일 </td>
      <td>X</td>
    </tr>
    <tr>
      <td>registrationNumber</td>
      <td>String</td>
      <td>환자등록번호</td>
      <td>X</td>
    </tr>
    <tr>
      <td>birthDate</td>
      <td>String</td>
      <td>생년월일</td>
      <td>X</td>
    </tr>
  </table>

### HTTP Response

**Example**
```java
{
    "resultCode": "SUCCESS",
    "result": {
    "patients": [
    {
      "name": "김혜린",
      "registrationNumber": "2023000002",
      "genderCode": "F",
      "birthDate": "1994-04-25",
      "phoneNumber": "010-1234-5678",
      "latestVisit": "2022-03-16"
      } ...
    ],
    "page": {
      "page": 1,
      "size": 10,
      "totalPages": 1,
      "totalCount": 2
    }
  }
}
```

| Name                                 | Type    | Description        |
|--------------------------------------|---------|--------------------|
| resultCode                           | String  | 결과 코드              |
| result.patients[].name               | String  | 환자명                |
| result.patients[].registrationNumber | String  | 환자등록번호             |
| result.patients[].genderCode         | String  | 성별                 |
| result.patients[].birthDate          | String  | 생년월일               |
| result.patients[].phoneNumber        | String  | 휴대전화번호             |
| result.patients[].latestVisit        | String  | 최근방문               |
| page.page                            | Number  | 현재 페이지             |
| page.size                            | Number | 현재 페이지에서 보이는 데이터 수 |
| page.totalPage                       | Number | 전체 페이지 수           |
| page.sort                            | Number  | 전체 데이터 수           |

---


## 환자 조회 API


### 기본 정보
```jsx
GET /v1/patients/{patientId} HTTP/1.1
Host: http://localhost:8080/v1/patients/1
```


### HTTP Response

**Example**
```java
{
    "resultCode": "SUCCESS",
    "result": {
    "patientId": 1,
    "name": "김혜린",
    "registrationNumber": "2023000001",
    "genderCode": "F",
    "birthDate": "1994-04-25",
    "phoneNumber": null,
    "hospital": {
      "id": 1,
      "name": "사랑병원",
      "careFacilityNumber": "230620-23db3b2f995b",
      "directorName": "김병원장"
    },
    "visits": [
      {
        "id": 1,
        "receivedAt": "2022-03-16 10:30:00",
        "visitStatusCode": "2"
      }
    ]
  }
}
```

| Name                               | Type   | Description |
|------------------------------------|--------|-------------|
| resultCode                         | String | 결과 코드       |
| result.patientId                   | Number | 환자 ID       |
| result.name                        | String | 환자명         |
| result.registrationNumber          | String | 환자등록번호      |
| result.genderCode                  | String | 성별코드        |
| result.birthDate                   | String | 휴대전화번호      |
| result.hospital.id                 | Number | 병원 ID       |
| result.hospital.name               | String | 병원명         |
| result.hospital.careFacilityNumber | String | 요양기관번호      |
| result.hospital.directorName       | String | 병원장명    |
| result.visits[].id                 | Number | 환자방문 ID     |
| result.visits[].receivedAt                 | String | 접수일시         |
| result.visits[].visitStatusCode                 | String | 방문상태코드      |

---


@@@@@@@@@@@

# 환자방문 API 명세


---

## 환자방문 등록 API

### 기본 정보
```jsx
POST /v1/visits HTTP/1.1
Host: http://localhost:8080/v1/visits
```

### HTTP Request

**Example**
```java
{
  "hospitalId" : 1,
  "patientId" : 1,
  "receivedAt" : "2022-03-16T10:30:00",
  "visitStatusCode" : 2
} 
```
<table>
    <tr>
      <td>Name</td>
      <td>Type</td>
      <td>Description</td>
      <td>Required</td>
    </tr>
    <tr>
      <td>hospitalId</td>
      <td>Number</td>
      <td>병원 ID</td>
      <td>O</td>
    </tr>
    <tr>
      <td>patientId</td>
      <td>Number</td>
      <td>환자 ID </td>
      <td>O</td>
    </tr>
    <tr>
      <td>receivedAt</td>
      <td>String</td>
      <td>접수일시 </br>
        'YYYY-mm-dd'T'HH:mm:ss' 형식 입력 </td>
      <td>O</td>
    </tr>
    <tr>
      <td>visitStatusCode</td>
      <td>String</td>
      <td>방문상태코드 </br>
        1 : 방문중 </br>
        2 : 종료 </br>
        3 : 취소</td>
      <td>O</td>
    </tr>
  </table>

### HTTP Response

**Example**
```java
{
  "resultCode": "SUCCESS"
}
```

| Name | Type | Description |
| --- | --- | --- |
| resultCode | String | 결과 코드 |

---

## 환자방문 수정 API


### 기본 정보
```jsx
PATCH /v1/visits/{visitId} HTTP/1.1
Host: http://localhost:8080/v1/visits/1
```

### HTTP Request

**Example**
```java
{
  "receivedAt" : "2022-03-17T10:30:00",
  "visitStatusCode" : 2
} 
```
<table>
    <tr>
      <td>Name</td>
      <td>Type</td>
      <td>Description</td>
      <td>Required</td>
    </tr>
       <tr>
      <td>receivedAt</td>
      <td>String</td>
      <td>접수일시 </br>
        'YYYY-mm-dd'T'HH:mm:ss' 형식 입력 </td>
      <td>X</td>
    </tr>
    <tr>
      <td>visitStatusCode</td>
      <td>String</td>
      <td>방문상태코드 </br>
        1 : 방문중 </br>
        2 : 종료 </br>
        3 : 취소</td>
      <td>X</td>
    </tr>
  </table>

### HTTP Response

**Example**
```java
{
  "resultCode": "SUCCESS"
}
```

| Name | Type | Description |
| --- | --- | --- |
| resultCode | String | 결과 코드 |

---


## 환자방문 삭제 API


### 기본 정보
```jsx
DELETE /v1/visits/{visitsId} HTTP/1.1
Host: http://localhost:8080/v1/visits/1
```

### HTTP Response

**Example**
```java
{
  "resultCode": "SUCCESS"
}
```

| Name | Type | Description |
| --- | --- | --- |
| resultCode | String | 결과 코드 |

---


## 환자방문 목록 조회 API


### 기본 정보
```jsx
GET /v1/visits/patients/{patientId} HTTP/1.1
Host: http://localhost:8080/v1/visits/patients/1?pageNo=1&pageSize=10
```

### HTTP Request

**Example**

<table>
    <tr>
      <td>Name</td>
      <td>Type</td>
      <td>Description</td>
      <td>Required</td>
    </tr>
    <tr>
      <td>patientId</td>
      <td>Number</td>
      <td>환자 ID</td>
      <td>O</td>
    </tr>
    <tr>
      <td>pageNo</td>
      <td>Number</td>
      <td>페이지 번호</td>
      <td>X</td>
    </tr>
    <tr>
      <td>pageSize</td>
      <td>Number</td>
      <td>페이지 사이즈</td>
      <td>X</td>
    </tr>
  </table>

### HTTP Response

**Example**
```java
{
    "resultCode": "SUCCESS",
    "result": {
        "visits": [
            {
                "id": 2,
                "hospitalId": 1,
                "patientId": 1,
                "receivedAt": "2022-03-17 10:30:00",
                "visitStatusCode": "1"
            },
            {
                "id": 1,
                "hospitalId": 1,
                "patientId": 1,
                "receivedAt": "2022-03-15 10:30:00",
                "visitStatusCode": "3"
            }
        ],
        "page": {
            "page": 1,
            "size": 10,
            "totalPages": 1,
            "totalCount": 2
        }
    }
}
```

| Name                                 | Type    | Description        |
|--------------------------------------|---------|--------------------|
| resultCode                           | String  | 결과 코드              |
| result.visits[].id               | Number  | 환자방문ID                |
| result.visits[].hospitalId | Number  | 병원ID             |
| result.visits[].patientId         | Number  | 환자ID                 |
| result.visits[].receivedAt          | String  | 접수일시               |
| result.visits[].visitStatusCode        | String  | 방문상태코드             |
| page.page                            | Number  | 현재 페이지             |
| page.size                            | Number | 현재 페이지에서 보이는 데이터 수 |
| page.totalPage                       | Number | 전체 페이지 수           |
| page.sort                            | Number  | 전체 데이터 수           |





