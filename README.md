# 주택 금융 서비스 API 개발
2019 카카오페이 경력 서버개발자 과제

## 설명 
국내 주택금융 신용보증 기관으로부터 년도별 각 금융기관(은행)에서 신용보증한 금액에 대한 데이터가 주어집니다. 이를 기반으로 아래 기능명세에 대한 API 를 개발하고 각 기능별 Unit Test 코드를 개발하세요.

## 데이터
- 주택금융 공급현황 분석 데이터 (첨부)
- 데이터는 csv 파일이며 각 레코드에 컬럼값은 ‘,’ 구분자로 저장이 되어 있음.

## 기본 제약 사항
- API 기능명세에서 기술한 API를 모두 개발
- 데이터 영속성 관리 및 매핑을 위한 ORM(Object Relational Mapping)을 사용하여 각 엔티티를 정의하고 Repository를 개발
  - Java의 경우 JPA, Phython의 경우 SQLAlchemy 적용 
  - 단, 엔티티 디자인은 지원자의 문제해결 방법에 따라 자유롭게 구성
  - 단, 주택금융 공급기관은 독립 엔티티(기관명과 기관코드)로 디자인 {“institute_name”, “institute_code”}
- 단위 테스트(Unit Test) 코드를 개발하여 각 기능을 검증
- 모든 입/출력은 JSON 형태로 주고 받음.
- README.md 파일을 추가하여, 개발 프레임워크, 문제해결 전략, 빌드 및 실행 방법을 기술
- 단, 프로그램 언어는 평가에 방영되지 않으니 자유롭게 선택
- 단, 각 API에 HTTP Method들(GET|POST|PUT|DELETE)은 자유롭게 선택

## 추가 제약 사항
- API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 호출 기능을 개발하고 각 API 호출 시에 HTTP Header에 발급받은 토큰을 가지고 호출
  - signup 계정 생성 API: 입력으로 ID, PW 받아 내부 DB에 계정 저장하고 토큰을 생성하여 출력
    - 단, 패스워드는 인코딩하여 저장
    - 단, 토큰은 특정 secret으로 서명하여 생성
  - signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급
  - refresh 토큰 재발급 API: 기존에 발급받은 토큰을 Authorization 헤더에 "Bearer Token"으로 입력 요청을 하면 토큰을 재발급

## 개발 프레임워크
- 개발 언어: Java
- 개발 프레임워크: spring boot
- Dependency

## 문제해결 전략

## 빌드 및 실행 방법

## 테이블 스키마
![Alt text](https://github.com/Khafre-SungMin-Cho/housing-finance/blob/master/Untitled%20Diagram.png)

### Institute (기관)



### 

## API 명세서
### POST /upload
데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API

- Request
  - 없음

- Response

응답 예시
<pre><code>{
  "errorCode": 0,
  "errorMessage": "success"
}
</code></pre>
  
- Response Code

### GET /banks
주택금융 공급 금융기관(은행) 목록을 출력하는 API

- Request
  - 없음

- Response

응답 예시
<pre><code>[
    {
        "instituteCode": "bnk-1",
        "instituteName": "주택도시기금1"
    },
    {
        "instituteCode": "bnk-2",
        "instituteName": "국민은행"
    },
    ...
]
</code></pre>

  
- Response Code

### GET /years
년도별 각 금융기관의 지원금액 합계를 출력하는 API

- Request
  - 없음

- Response

응답 예시
<pre><code>[
  {
      "year": 2016,
      "totalAmount": 400971,
      "detailAmount": {
          "농협은행/수협은행": 23913,
          "하나은행": 45485,
          "우리은행": 45461,
          "국민은행": 61380,
          "신한은행": 36767,
          "외환은행": 5977,
          "주택도시기금1": 91017,
          "기타은행": 90925,
          "한국시티은행": 46
      }
  },
  {
      "year": 2017,
      "totalAmount": 295126,
      "detailAmount": {
          "농협은행/수협은행": 26969,
          "하나은행": 35629,
          "우리은행": 38846,
          "국민은행": 31480,
          "신한은행": 40729,
          "외환은행": 0,
          "주택도시기금1": 85409,
          "기타은행": 36057,
          "한국시티은행": 7
      }
  },
  ...
]
</code></pre>
  
- Response Code

### GET /largest
각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API

- Request
  - 없음

- Response

응답 예시
<pre><code>{
  "instituteName": "주택도시기금1",
  "year": 2014
} 
</code></pre>
  
- Response Code

### GET /summary/{bank_code}
전체 년도(2005~2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API

- Request
  - bank: institute code, string, 뱅크 코드

- Response

응답 예시
<pre><code>{
  "instituteName": "주택도시기금",
  "supportAmount":[
    {"year":2008,"amount":78},
    {"year":2015,"amount":1702}
  ]
} 
</code></pre>
  
- Response Code


## 선택 문제 API 명세
### 예측 API
특정 은행의 특정 달에 대해서 2018 년도 해당 달에 금융지원 금액을 예측하는 API
- 단, 예측 알고리즘을 무엇을 써야하는지에 대한 제약은 없지만, 가장 근사치에 가까울 수록 높은 점수 부여.

## TO DO
- [X] README 정리
- [X] 테이블 스키마 설계
- [ ] API 명세서 작성
  - [ ] request 작성
  - [ ] response 작성
  - [ ] response 예시
  - [ ] result code 작성
- [X] REST API 프로젝트 템플릿 만들기
- [X] H2 Database + Hibernate 연동
- [X] 데이터 읽어오기
- [X] Identifier generator 생성 및 적용
- [ ] 주택금융 공급 금융기관(은행) 목록을 출력하는 API
- [ ] 년도별 각 금융기관의 지원금액 합계를 출력하는 API
- [ ] 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API
- [ ] 전체 년도에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API
- [ ] 예외 처리

## OPTION
- [ ] 추가 제약 사항
- [ ] 선택 문제 API
- [ ] 데이터 파일 Charset에 따른 동적 읽어오기
