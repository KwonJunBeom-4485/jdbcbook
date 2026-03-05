[ cli 기반 JAVA & JDBC 도서 관리 시스템 ]

1. MySQL 기반 DB 환경을 구축.
 - DBeaver로 localhost로 Connection 설정을 마친다. (권한 설정도 다 주기?)
 - mysql 기반, url(jdbc:mysql://localhost:3306/jdbc), Username(jdbcuser), password(jdbcuser)
 - libraryTestQuary.sql 파일로 해당 환경에서 DB와 테이블을 생성한다.

2. 필요하다면 JAVA PROJECTS에 필요한 Referenced Libraries에 넣을 MySQL과 Lombok Driver를 다운해 넣어준다.
 - https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.6.0/mysql-connector-j-9.6.0.jar
 - https://repo1.maven.org/maven2/org/projectlombok/lombok/1.18.42/lombok-1.18.42.jar

3. 코드를 정상 작동하기 위한 기본적인 익스텐션도 깔아준다.(수업에서 깐 것만 있어서 상관x)

4. 작동시킨다.