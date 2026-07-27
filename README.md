# Spring Advanced (org.example.expert)

## 목차
- [프로젝트 소개](#프로젝트-소개)
- [개발 기간](#개발-기간)
- [주요 기능](#주요-기능)
- [트러블슈팅](#트러블슈팅)

---

## 프로젝트 소개

Spring Boot 기반의 일정(Todo) 관리 API 서버입니다. 회원가입/로그인, 일정(Todo) 생성 및 조회, 담당자(Manager) 지정, 댓글(Comment) 작성, 관리자(Admin) 권한 기반 API를 제공하며, 기존 프로젝트에 남아있던 문제들을 직접 찾아 개선하는 과제로 진행했습니다.

- **기술 스택**: Java 21, Spring Boot 3.3.3, Spring Data JPA, MySQL, JWT, Gradle
- **저장소**: [github.com/Yong-B/spring-advanced](https://github.com/Yong-B/spring-advanced)

## 개발 기간

2026.07.23 ~ 2026.07.27

## 주요 기능

### 1. `@Auth` 인증 파라미터 리졸버 정상화
- `AuthUserArgumentResolver`가 Spring MVC에 등록되어 있지 않아 동작하지 않던 문제를 `WebConfig`(`WebMvcConfigurer`) 추가로 해결
- 컨트롤러에서 `@Auth AuthUser` 파라미터로 로그인 유저 정보를 정상적으로 주입받을 수 있도록 개선

### 2. 코드 개선
- **Early Return**: `AuthService.signup()`에서 이메일 중복 검증을 비밀번호 암호화보다 먼저 수행하도록 순서 조정, 불필요한 연산 방지
- **불필요한 if-else 제거**: `WeatherClient.getTodayWeather()`의 중첩 조건문을 단순화
- **Validation 분리**: `UserService.changePassword()`의 비밀번호 형식 검증 로직을 서비스 코드에서 요청 DTO(`@Valid`, Bean Validation)로 이동

### 3. N+1 문제 해결
- `TodoRepository.findAllByOrderByModifiedAtDesc()`에서 JPQL fetch join 대신 `@EntityGraph(attributePaths = {"user"})`를 사용하도록 변경하여, 목록 조회 시 발생하던 N+1 문제 해결

### 4. 테스트 코드 정비
- `PasswordEncoderTest`: `matches()` 호출 시 인자 순서 오류 수정
- `ManagerServiceTest`: 실제 예외 타입/메시지에 맞지 않던 테스트 어서션 및 메서드명 수정
- `CommentServiceTest`: 기대 예외 타입을 실제 발생 예외(`InvalidRequestException`)에 맞게 수정
- `ManagerService.saveManager()`: `Todo.user`가 `null`인 경우 NPE 대신 `InvalidRequestException`을 던지도록 null 체크 추가

### 5. 관리자 API 접근 로깅 (Interceptor + AOP)
- `AdminAccessInterceptor`: `/admin/**` 요청에 대해 관리자 권한 여부를 사전 검증하고, 인증 성공 시 요청 시각/URL 로깅
- `AdminApiLoggingAspect`: `@Around` AOP를 통해 관리자 API(`CommentAdminController.deleteComment`, `UserAdminController.changeUserRole`) 실행 전후로 요청 유저 ID, 요청 시각, URL, 요청/응답 본문을 JSON 형태로 로깅

### 6. 자체 개선 — 외부 API 장애 격리
- `TodoService.saveTodo()`가 외부 날씨 API(`WeatherClient`) 실패 시 함께 실패하던 구조를 개선
- 날씨 조회 실패를 흡수하여 기본값으로 대체, 부가 기능(날씨) 장애가 핵심 기능(Todo 생성)에 영향을 주지 않도록 분리


