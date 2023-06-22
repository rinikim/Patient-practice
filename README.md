# 환자 API 명세

### 🔗 깃허브에 프로젝트와 이슈를 작성하였습니다.

url : https://github.com/users/rinikim/projects/2/views/1

### 💻 H2 DB 접속 url 입니다.

- 서버 실행 시 병원, 코드그룹, 코드 테이블의 초기 데이터가 생성됩니다.
    - 병원, 코드그룹, 코드에 기본적인 데이터 저장(data.sql 참고)
    - application.yml 설정
        - jpa.defer-datasource-initialization: true -> script 파일이 hibernate 초기화 이후 동작하게 하기 위한 옵션입니다.
        - sql.init.mode: always -> 서버 시작시 항상 classpath의 sql문을 실행하도록 설정합니다.
        - sql.init.continue-on-error: true -> 애플리케이션을 시작하는 동안 SQL 오류를 무시하도록 설정합니다. (SQL 오류가 발생하더라도 애플리케이션이 계속 시작됩니다.)
        - sql.init.data-locations: classpath:sql/data.sql -> 서버 시작시 dml sql 문을 실행할 위치 및 파일 지정합니다. (resources/sql/data.sql 실행됩니다.)


url : http://localhost:8080/h2-console

### 💾 Entity 설명

**테이블 관계**
- 병원과 환자는 양방향 관계를 맺고 있습니다.
    - 병원과 환자는 각각의 엔티티를 서로 조회해야 할 일이 많을 것 같아 양방향으로 관계를 맺었습니다.
- 환자와 환자방문은 양방향 관계를 맺고 있습니다.
    - 환자에서 환자방문에 대해 조회 할 일이 많을 것 같지만, 일대다 단방향은 실무적으로 추천하지 않아 양방향 관계로 맺었습니다.
        -  일대다 단방향 단점
        1. 엔티티가 관리하는 외래 키가 다른 테이블에 있음 (Many에 외래키 존재)
        2. 연관관계 관리를 위해 추가로 update sql 실행 (성능상 큰 차이는 없다)
        3. 개발을 하다 보면 B를 만졌는데 A도 update sql문 발생
          -> 그래서 필요하다면 일대다 보다는 양방향 관계로 한다. ( B는 A가 필요 없더라도, 객체 지향적으로 손해를 보는 거 같지만) - 트레이드 오프
- 병원과 환자방문은 다대일 단방향만 맺고 있습니다.
    - 병원에서 환자방문에 대한 이력을 조회하는 경우는 드물고(환자방문을 조회해오게 되면 환자의 정보도 필수적일 것이라 생각했습니다.) 
    - 환자방문이 필요할 때에는 병원 ID를 기준으로 조회해오는 것이 더 좋을 것 같아 다대일 단방향으로 맺었습니다.

- 정책에 따라 다르겠지만, 환자가 삭제되어있을 때 환자방문을 따로 이력을 조회하는 경우는 적을 것 같아 orphanRemoval 옵션을 걸어줬습니다.
- 병원을 삭제했을 때 환자정보는 남아있어야 할 것 같아 orphanRemoval 옵션을 걸지 않았습니다.

**`코드그룹` 테이블 설명**

- 환자, 환자방문 저장 및 수정 시 임의의 값을 넣는 것을 방지하기 위해 코드 확인 로직 추가했습니다.
    - 코드그룹에 맞는 코드인지 확인하기 위해 코드 테이블을 조회하여 확인하는 로직들을 추가했습니다. 
- Enum 으로 관리하게 되면 코드그룹 테이블에 데이터가 추가될 때마다 Enum 에도 값을 추가하여 배포해야되기 때문에 이를 방지하기 위해 코드그룹 테이블 생성 및 관리하기 위해 코드그룹과 코드테이블을 생성했습니다.


**추가 된 필드**

- 환자 테이블의 삭제 필드는 추후에 언제 데이터가 필요하게 될지 몰라 Soft Delete 처리를 해줬습니다.
    - 환자 데이터를 삭제할 때 DB DELETE 쿼리문은 추후 환자 데이터에대해 추적하기 힘들기 때문에 삭제여부(`deleted`) 필드를 추가하여 환자 삭제를 관리합니다.
- 생성일자와 수정일자가 있어야 추후 히스토리를 찾기 쉬어 추가했습니다.
    - JPA Auditing 으로 처리했습니다.

**인덱스**
- 환자목록 조회 시 `환자명`, `환자등록번호`, `생년월일`을 기준으로 조회를 하기 때문에 환자 테이블에 각각의 인덱스를 생성해줬습니다.

---

### 💡 API는 RESTful API 형식에 맞게 작성했습니다.

# 환자 API

## 환자 등록 API

**환자등록번호 생성**

- 환자등록번호는 환자 테이블에서 지정 된 최대 문자 길이인 varchar(13)보다 짧게 만들었습니다.
- 환자등록번호는 병원별로 중복되지 않도록 생성해주었습니다.
- 환자등록번호는 기획에 맞게 `현재연도 + 현재 연도별로 등록 된 환자 수 + 1`의 계산식을 기준으로 생성했습니다.
- 현재 연도별로 등록 된 환자 수는 삭제처리 된 환자까지 포함하여 조회합니다.(기존에 생성했지만 삭제한 환자들도 있기 때문에 일관성 유지를 위해 삭제된 환자까지 조회합니다.)
  - 예 : 201900085, 201900086

**환자 등록**
- 환자 Entity 에서 Not Null 조건이 있는 필드는 요청 데이터에 Not Null 조건 처리했습니다.
- 환자를 등록하기 이전 등록 된 병원이 없을 경우 Exception이 반환됩니다.


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
        ('yyyy-MM-dd'으로 입력)</td>
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

- 환자가 등록 된 병원이 바뀔 경우는 없기 때문에 환자의 병원과 환자등록번호는 변경하지 않았습니다.
- 환자의 정보를 수정할 땐 전체 정보를 수정할 경우보다 각각의 정보를 수정할 경우가 많기 때문에 HTTP METHOD 를 PATCH 로 설정했습니다.
- 환자 정보를 수정할 시 조회되는 환자가 없으면 Exception이 반환됩니다.

**💡 동시성 고려하지 않았습니다.**

- 현재 기획에서 환자에 대한 수정을 동시에 할 일을 제시하지 않아 동시성을 고려하지 않고 구현했습니다.
    - 추가적으로 환자의 접수를 받는 사람은 보통 1명이라 판단하여 동시성을 고려하지 않았습니다.
- 하지만, 동시성을 고려한다면 `낙관적 락(Optimistic Lock)`을 적용할 것입니다.
    - 환자의 접수를 받는 사람은 보통 1명일 것이고 동시에 정보를 수정하는 경우는 거의 없을 것이라 판단하였습니다.
    - Patient Entity 에서 @Version 을 사용하여 낙관적 락을 적용할 것입니다.
    - 낙관적 락은 동시에 접속 후 수정하게 되면 뒤늦게 수정한 사용자에게 Exception을 반환합니다.
    - 이를 캐치하여 사용자에게 알려주고 다시 수정하도록 유도할 것입니다.


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
        ('yyyy-MM-dd'으로 입력)</td>
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

- 환자가 존재하지 않을 경우 Exception이 반환됩니다.
- 환자 데이터를 삭제할 때 DB DELETE 쿼리문은 추후 환자 데이터에대해 추적하기 힘들기 때문에 삭제여부 필드를 추가하여 Soft Delete 처리를 했습니다. (삭제 시 `deleted` 필드를 true 로 변경해줍니다.)


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

- 목록조회 시 params 에 있는 데이터만 조건을 적용해서 조회합니다. (환자명, 환자등록번호, 생년월일을 필드별조회, 모두조회 가능합니다.)
- 클라이언트에서의 페이지의 `시작번호는 1`이지만 서버 내부적으로 시작되는 페이지는 0이기 때문에 -1 처리를 해줍니다.
- Left Join 으로 한 이유는 환자방문이 조회되지 않더라도 환자를 조회하기 위해 사용했습니다.
- QueryDsl 로 조회 후 DTO 로 바로 데이터를 받기 위해 Projections 사용했습니다.
- page 관련 response class 를 따로 생성하여 관리합니다.
    - 페이지정보는 현재 페이지, 현재 페이지에서 보이는 데이터 수, 전체 페이지 수, 전체 데이터 수로 구성됩니다.
- 전체 환자 수가 0명이면 빈 배열로 결과값이 반환됩니다.
- 전체 데이터 수가 0개이면 전체 페이지는 0을 반환됩니다.
- 환자 목록은 모든 병원에 있는 환자 목록을 가져오는 것이 아닌, 하나의 병원 내부에 있는 환자 목록을 가져오는 것이므로 병원 ID 조건이 필수적이어야 합니다.
- 페이지 관련 요청 데이터도 환자 목록 조회 시에 필요한 요청 데이터이기 때문에 DTO 내부에 넣어줌으로써 가독성을 높혔습니다.
- 환자방문이 존재하지 않을 경우에는 `null`을 반환하고, 존재할 경우에는 가장 최근의 환자방문 이력을 반환합니다.
    - 환자방문은 방문상태코드가 `3(취소)` 일 경우에는 조회 시 제외했습니다.
- 환자 조회에 대한 정렬은 `환자 ID 역순`으로 진행합니다.(기획에 따라 변경이 가능합니다.)

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
      <td>페이지 번호 </br>
            (1부터 시작 / 기본 페이지 : 1)</td>
      <td>X</td>
    </tr>
    <tr>
      <td>pageSize</td>
      <td>Number</td>
      <td>페이지 사이즈</br>
        (기본 사이즈 : 10)</td>
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
      <td>생년월일 </br>
        ('yyyy-MM-dd' 으로 입력)</td>
      <td>X</td>
    </tr>
    <tr>
      <td>registrationNumber</td>
      <td>String</td>
      <td>환자등록번호</td>
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

- 환자, 병원, 환자방문 정보 모두 조회합니다.
- 환자가 없을 경우 Exception이 반환됩니다.
- 현재 환자방문 조회 시 목록은 ID 를 기준으로 역순차순으로 조회합니다.
    - 추후 환자방문에 대한 페이징 처리가 필요하다면 환자방문 조회 API를 함께 사용할 수 있습니다. (기존에 조회되는 환자방문 내역은 삭제) 
- 환자방문이 없을 경우에는 빈 배열로 반환합니다.

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


# 환자방문 API 명세

## 환자방문 등록 API

- Request Data 중 방문상태코드가 존재하는 코드인지 확인합니다.
- 병원이 존재하지 않을 경우 Exception이 반환됩니다.
- 환자가 존재하지 않을 경우 Exception이 반환됩니다.

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
        ('YYYY-mm-dd'T'HH:mm:ss' 으로 입력) </td>
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

- 환자방문은 진료 당 환자방문 1 row 입니다. (환자가 방문해서 진료를 받게 되면 `방문중 -> 종료` 상태값이 변경됩니다.)
- 병원과 환자가 변경될 일은 없기 때문에 `접수일시`와 `방문상태코드`만 변경할 수 있습니다.
- 동시에 수정을 할 경우보다 각각 수정이 일어날 경우가 많아 HTTP METHOD 를 PATCH 로 설정했습니다.
- Request Data 중 방문상태코드가 존재하는 코드인지 확인합니다.
- 환자방문내역이 존재하지 않을 경우 Exception이 반환됩니다.
- 환자방문 삭제 API가 있지만, 요청 환자방문상태코드 데이터가 `취소(3)`인경우에도 변경하도록 허용했습니다.


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
        ('YYYY-mm-dd'T'HH:mm:ss' 으로 입력)</td>
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

- 환자방문 조회 시 조회되지 않으면 Exception이 반환됩니다.
- 코드테이블에서 방문상태코드 중 `3`은 `취소`를 의미하기 때문에 삭제 할 때 방문상태코드를 `3`으로 변경해줍니다.
    - 위의 취소가 `예약취소`를 의미한다면 환자방문 삭제 기능을 Soft Delete 처리로 변경해주거나 Delete Query 로 변경할 수 있습니다.

### 기본 정보
```jsx
DELETE /v1/visits/{visitsId} HTTP/1.1
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

- 전체 환자방문 목록조회는 기획된 것이 없고, 사용할 일이 없을 것이라고 판단하여 환자별 환자방문 목록 조회기능만 구현했습니다.
- 환자 ID 를 기준으로 환자방문 목록을 조회해옵니다.
- 페이징 처리 적용했습니다. (페이지 시작번호: 1, 기본 size : 10, 정렬: 환자방문 ID 역순)
    - 환자 목록 조회의 페이지 번호가 1부터 시작하기 때문에 일관성을 위해 페이지 시작을 1로 설정했습니다.
    - 정렬은 접수일시 순으로 변경할 수 있습니다. (현재는 화면에서 번호를 보여준다는 전제하에 ID를 기준으로 역순했습니다.)
- 환자방문 조회 시 내역이 없을 경우에는 Exception이 반환됩니다.
- 환자 조회 API 에서 환자방문 이력을 조회할 때 해당 Endpoint 를 사용하면 환자방문 이력조회 시 페이징처리가 되어있어 편하게 관리할 수 있을 것이라고 생각했습니다.
- 환자방문은 방문상태코드가 `취소(3)`인 경우에도 조회되도록 허용했습니다.


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
      <td>페이지 번호</br>
        (1부터 시작 / 기본 페이지 : 1)</td>
      <td>X</td>
    </tr>
    <tr>
      <td>pageSize</td>
      <td>Number</td>
      <td>페이지 사이즈 </br>
        (기본 사이즈 : 10)</td>
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





