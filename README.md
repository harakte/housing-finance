# 주택 금융 서비스 API

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
- 개발 프레임워크: Spring boot
- Dependency
  - org.springframework.boot:spring-boot-starter-web
  - org.springframework.boot:spring-boot-starter-data-jpa
  - com.h2database:h2
  - com.opencsv:opencsv
  - org.projectlombok:lombok
  - org.springframework.boot:spring-boot-starter-test

## 문제해결 전략
- Entity 관계
  - 양방향 OneToMany 관계로 개발
  - 요구사항에 따라 기관을 기준 또는 금융데이터를 기준으로 처리하기 위해서 서로 연관
- 각 년도별 은행별 금액 계산
  - 금융데이터를 기준으로 년도로 그룹핑
  - 년도별로 그룹핑된 금액을 합쳐서 년도 금액 총액을 계산
- 각 년도별 각 기관의 가장 큰 금액 찾기
  - 기관을 기준으로 기관의 년도별 금액을 계산
  - 기관 내에서 가장 큰 년도 금액 찾기
  - 기관별 가장 큰 년도 금액끼리 비교하여 가장 큰 값 찾기
- 지원금액 평균 중에서 가장 작은 금액과 큰 금액
  - 기관의 금융 정보를 년도로 그룹화해서 평균 계산
  - 년도별 평균 금액을 정렬
  - 오름차순 기준, 최소값은 가장 첫 값, 최대값은 가장 마지막  
- 2018 년도 금융지원 금액을 예측하는 방법
  - 예측 방법
    - 각 년도별 각 달의 금융지원 금액이 선형 관계가 있다고 가정
    - 위와 같은 가정을 했을 경우, Linear regression(선형 회귀) 사용이 적합.
    - 예측 변수를 무엇으로 할 것인지 고려
      - 1안. 매년 같은 달을 예측 변수로 잡는다. 
        - 장점: 어떤 달인지에 따라 금융 금액 지원이 달라질 수 있는 점이 반영 가능(예. 매년 1월은 지원 금액이 많고, 12월은 적다.)
        - 단점: 예측에 사용할 수 있는 데이터가 줄어듦. 정확도가 내려갈 수 있음.
      - 2안. (년,달)을 총 달로 계산하여 예측변수로 잡는다.
        - 장점: 기관의 모든 금융 데이터를 활용가능, 예측 변수간 거리가 더 조밀(x간 거리: 1달)
        - 단점: 달마다 외부 요인으로 변동되는 지원 금액을 반영 불가
    - 전체 금융 데이터가 적고, 외부 요인보다는 데이터를 통한 회귀 계수가 더 중요할 것으로 생각하여 2안으로 진행

## 빌드 및 실행 방법

csv 파일 위치
csv/input.csv

jar 다운로드: [jar 다운로드](https://github.com/Khafre-SungMin-Cho/housing-finance/raw/master/libs/housing-finance-1.0.0.jar)

빌드
<pre><code>./gradlew build</code></pre>

실행
<pre><code>java -jar .\build\libs\housing-finance-1.0.0.jar</code></pre>

## 테이블 스키마
![Alt text](https://github.com/Khafre-SungMin-Cho/housing-finance/blob/master/Untitled%20Diagram.png)

### Institute (기관)
- institute_code: String
- institute_name: String

### Finance (금융 데이터)
- institute_code: String
- year: Integer
- month: Integer
- amount: Integer

## API 명세서
### POST /upload
데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API

- Request
  - http://localhost:8080/upload
  - body parameter
  	- 없음

- Response
  - result: boolean, 데이터 파일 저장의 성공 여부

응답 예시
<pre><code>{
  "success":true
}</code></pre>

### GET /banks
주택금융 공급 금융기관(은행) 목록을 출력하는 API

- Request
  - http://localhost:8080/banks
  - body parameter
  	- 없음
- Response
  - instituteCode: String, 기관 코드
  - instituteName: String, 기관명

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

### GET /years
년도별 각 금융기관의 지원금액 합계를 출력하는 API

- Request
  - http://localhost:8080/years
  - body parameter
  	- 없음
	
- Response
  - year: Integer, 년도
  - totalAmount: Integer, 년도의 총 지원금액(억원)
  - detailAmount: <String, Integer>, <기관명, 지원금액(억원)>
  
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

### GET /largest
각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API

- Request
  - http://localhost:8080/largest
  - body parameter
    - 없음

- Response
  - instituteName: String, 기관명
  - year: Integer, 년도

응답 예시
<pre><code>{
  "instituteName": "주택도시기금",
  "year": 2014
} 
</code></pre>

### GET /summary/{bankCode}
전체 년도(2005~2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API

- Request
  - http://localhost:8080/summary/bnk-8
  - path parameter
    - bankCode: String, 외환은행 기관코드(institute code) 입력(bnk-8)

- Response
  - instituteName: String, 기관명
  - supportAmount: 년도별 평균 지원금액 최소값, 최대값
    - year: Integer, 년도
    - amount: Integer, 년도 평균 지원금액

응답 예시
<pre><code>{
  "instituteName": "주택도시기금",
  "supportAmount":[
    {"year":2008,"amount":78},
    {"year":2015,"amount":1702}
  ]
} 
</code></pre>

## 선택 문제 API 명세
### 예측 API
특정 은행의 특정 달에 대해서 2018 년도 해당 달에 금융지원 금액을 예측하는 API
- 단, 예측 알고리즘을 무엇을 써야하는지에 대한 제약은 없지만, 가장 근사치에 가까울 수록 높은 점수 부여.

POST /predict

- Request
  - http://localhost:8080/predict
  - content-type: application/json
  - body parameter
    - instituteName: String, 기관명
    - month: Integer, 예측 달
<pre><code>{
	"instituteName":"국민은행",
	"month":2
}</code></pre>

- Response
  - instituteCode: 기관 코드
  - year: Integer, 년도
  - month: Integer, 달
  - amount: Integer, 예측 지원금액
<pre><code>{
    "instituteCode": "bnk-2",
    "year": 2018,
    "month": 2,
    "amount": 4880
}</code></pre>

## TO DO
- [X] README 정리
- [X] 테이블 스키마 설계
- [X] API 명세서 작성
  - [X] request 작성
  - [X] response 작성
  - [X] response 예시
- [X] REST API 프로젝트 템플릿 만들기
- [X] H2 Database + Hibernate 연동
  - [X] H2 Database 연동
  - [X] Hibernate 연동
  - [X] Entity 구성
- [X] 데이터 읽어오기
- [X] Identifier generator 생성 및 적용
- [X] 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API
  - [X] Happy case만 고려된 동작 개발
  - [X] 예외처리 작업
- [X] 주택금융 공급 금융기관(은행) 목록을 출력하는 API
  - [X] Happy case만 고려된 동작 개발
  - [X] 예외처리 작업 
- [X] 년도별 각 금융기관의 지원금액 합계를 출력하는 API
  - [X] Happy case만 고려된 동작 개발
  - [X] 예외처리 작업
- [X] 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API
  - [X] Happy case만 고려된 동작 개발
  - [X] 예외처리 
- [X] 전체 년도에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API
  - [X] Happy case만 고려된 동작 개발
  - [X] 예외처리 
- [X] 응답 처리
  - [X] 결과 응답 처리
  - [X] 예외 응답 처리

## OPTION
- [ ] 추가 제약 사항(JWT)
  - [ ] signup 계정 생성 API
  - [ ] signin 로그인 API
  - [ ] refresh 토큰 재발급 API
- [X] 특정 은행의 특정 달에 대해서 2018 년도 해당 달에 금융지원 금액을 예측하는 API
  - [X] 예측 알고리즘 선정
  - [X] 예측 알고리즘 구현
  - [X] 예외 처리
