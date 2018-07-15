
# 소개
1. springboot2와 angular4를 사용한 single page application입니다.
2. jpa와 security를 사용하여 영속화와 인증을 구현
3. Java 8 or Java 9
4. Spring Boot 2.0.3
5. Maven
6. DB는 H2 database를 사용

# 구현
1. 카카오 책 검색 api 이용
2. 새로운 API 추가를 위한 @ApiVersion 대응
3. 회원가입 및 로그인 기능 구현(암호화 적용)
4. 책 검색(sorting, category 검색 지원)
5. 책 상세정보 및 최근 검색 히스토리
6. 북마크(추가 / 삭제)
7. H2 database 클라이언트 제공
8. swagger ui 제공

# 외부 라이브러리
1. spring-boot(web, test, jpa, security)
2. io.jsonwebtoken
3. com.h2database
4. org.projectlombok
5. springfox-swagger2

# 실행 방법
1. maven 프로젝트이므로 maven spring-boot:run 으로 실행 가능합니다.
2. 만약 IDE에서 실행하고 싶다면, Application.java의 main함수를 run 합니다. 
3. http://localhost:8000 에서 서비스 확인  

# 주의사항
1. 만약 IntelliJ를 사용하여 실행한다면 [여기](http://blog.woniper.net/229)를 참고하여 lombok 플러그인을 설치하셔야 합니다.
2. 어플리케이션은 Java 8 또는 9에서 동작합니다.
 

