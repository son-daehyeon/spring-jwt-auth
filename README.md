
# Spring Security (JWT) Template

## 프로젝트 소개

이 프로젝트는 Spring Security와 JWT(Json Web Token)을 활용하여 보안 인증 및 권한 관리를 구현한 템플릿입니다.

## 프로젝트 기술 스택

<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring_Boot_(v.%203.2.4)-F2F4F9?style=for-the-badge&logo=spring-boot">
<img src="https://img.shields.io/badge/Spring_Security-F2F4F9?style=for-the-badge&logo=springsecurity">
<img src="https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">

## 프로젝트 설정

### application.yaml 설정
프로젝트를 사용하기 전에 `src/resources/application_template.yaml` 파일을 복사하여 `src/resources/application.yaml`로 변경해야 합니다. 그리고 아래의 내용을 수정합니다.
```yaml  
spring:  
 data: 
  mongodb:
    host: localhost
    port: 27017
    username: (username)
    password: (password)
    database: (database)
    authentication-database: admin
  redis:
    host: localhost
    port: 6379
    password: (password)

app:  
 security:
  jwt-secret-key: (jwt_secret) 
  access-token-expirations-hour: 4
  refresh-token-expirations-hour: 168  
```  
괄호 안의 내용을 해당하는 값으로 변경해야 합니다.

## 인증 및 인가
API 요청 시 `accessToken`은 `Request Header`의 `Authorization`에 "Bearer (Token)" 저장하고, `refreshToken`은 `Request Cookie`의 `refresh_token`에 저장하여주세요.

만약 응답이 왔을 때 `accessToken`이 만료되었다면, 다음과 같이 Response Header에 새로운 `accessToken`과 `refreshToken`이 전송됩니다.
- `App-Reissue-Token: 1`
- `App-New-Access-Token`: 새로운 `accessToken`
- `App-New-Refresh-Token`: 새로운 `refreshToken`

## API 명세서

### 로그인
- **Method:** POST
- **Endpoint:** `/api/user`
- **Request Body:**
  ```json  
  {  
    "username": "(username)",  
    "password": "(password)"  
  }  
  ```  
- **Response Body:** 정상적으로 로그인되면 `accessToken`과 `refreshToken`이 응답으로 반환됩니다.

### 회원가입
- **Method:** PUT
- **Endpoint:** `/api/user`
- **Request Body:**
  ```json  
  {  
    "email": "(email)",
    "username": "(username)",  
    "password": "(password)"
  }  
  ```  
- **Response Body:** 정상적으로 회원가입되면 별도의 응답 바디는 없습니다.

## Axios Client
[프로젝트의 axios.js](https://github.com/son-daehyeon/spring-security-jwt-template/blob/main/axios.js) 파일 참고하세요.
