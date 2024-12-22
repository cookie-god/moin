# Moin Backend Test

## 기본 셋팅
### 1. 사용에 필요한 라이브러리 셋팅
- spring-boot-starter-web
- spring-boot-starter-jpa
- spring-boot-starter-security
- spring-boot-starter-aop
- spring-boot-starter-jdbc
- h2 database
- lombok
- swagger
- querydsl
- jwt
- logstash-logback
- webflux
### 2. config 파일 생성
- JwtTokenProvider 생성
  - JWT 생성 역할을 함
  - JWT 검증 역할을 함
  - JWT 추출 기능을 함
- Custom Filter 셋팅
  - Jwt 관련 필터 셋팅
    - Bearer 토큰으로 셋팅
    - 유저 정보 SecurityContext에 셋팅
    - 토큰 유효성 여부 확인
    - 에러 핸들링 처리
  - 로깅 관련 셋팅
    - request, response 관련 로깅 완료
- SecurityConfig 셋팅
  - 비밀번호 암호화 메서드 빈 등록
  - 필터 체인 메서드 빈 등록
  - Custom Filter 등록
- 전역 핸들러 등록
  - Custom Exception 클래스 생성
  - Client 및 Server 오류에 대응하기 위한 ErrorCode Enum 클래스 생성
  - 공통 응답을 표현하기 위한 BasicResponse와 ErrorReponse 클래스 생성
  - 전역 핸들러에 Custom Exception 관련 에러 핸들러 등록
- Swagger Config 생성
  - api 문서를 위해 생성
  - bearer 토큰 사용 여부 추가
- QueryDsl Config 생성
  - JPAQueryFactory 객체를 빈으로 등록
- WebClient Config 생성
  - WebClient 객체를 빈으로 등록
  - 실행시 request와 response를 로깅하도록 셋팅
- SecurityUtil Config 생성
  - SecurityContext에 존재하는 유저 정보 가져옴
- AESUtil
  - 주민등록번호 및 사업자 등록번호 암호화를 위해 사용
  - 현재는 동일한 암호키로 관리하도록 하였음.
### 3. enum 클래스 생성
- IdType에 대한 enum 클래스 생성
- Status에 대한 enum 클래스 생성
### 4. entity 클래스 생성
- BaseEntity 추상 클래스를 생성하여 공통된 필드 처리
- 각 Entity에 맞는 클래스 파일 생성
### 5. dto 클래스 생성
- 각 api의 도메인에 맞는 dto를 관리하기 위해 dto 클래스 파일 생성
- 도메인별 사용되는 dto는 static 중첩 클래스를 이용하여 관리하였음
### 6. controller, service, repository 클래스 생성
- 각 도메인에 맞는 controller, service, repository를 생성 하였음

## 1번 회원가입
### 1. request에 대한 validation 체크
- 각 request에 대한 존재 여부를 체크 하였음.
- 각 request의 정규식에 해당하는지 체크 하였음.
- 개인, 법인에 따른 validation 체크 완료 하였음.
- 고유한 userId에 대해서 존재 여부 체크 하였음.
### 2. User 엔티티 생성
- 유저 엔티티를 생성하는 메서드를 통해 유저 엔티티 생성
- SecurityConfig에서 생성한 BCryptPasswordEncoder를 이용하여 비밀번호 암호화 완료
- 주민번호 및 사업자 등록번호에 대해서 AES 알고리즘을 이용하여 암호화 완료
### 3. User 엔티티 데이터베이스에 삽입
- 유저 데이터를 JPARepository를 이용하여 데이터 삽입

## 2번 로그인
### 1. request에 대한 validation 체크
- 각 request에 대한 존재 여부를 체크 하였음.
- 각 request의 정규식에 해당하는지 체크 하였음.
### 2. User 엔티티 확인
- request로 전달받은 userId를 통해 유저 체크
- 만약 존재하지 않는다면 에러 처리 진행.
### 3. 암호화된 비밀번호 확인
- UserInfo 클래스에 정의된 비밀번호 암호화 확인 로직 이용
- 만약 일치하지 않는다면 에러 처리 진행
### 4. JWT 생성
- JwtTokenProvider를 통해 jwt 토큰 생성