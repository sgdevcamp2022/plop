# PLOP 서버 실행 참고자료

### 1. 각 서비스를 실행시키기 위한 jar 파일 생성

>1. cd plop/src/server
>2. cd ${server} // jar 파일을 생성하고자 하는 서버 폴더로 이동
>3. ./gradlew build -x test <br>
>4. 결과적으로 plop/src/server/${server}/build/libs/${server}-0.0.1-SNAPSHOT.jar 파일 생성

### 2. 추가 환경 변수 설정
보안을 위해 슬랙을 통해 전달

### 3. redis(캐시 서버) 실행
```bash
docker pull redis
docker run --name myredis -d -p 6379:6379 redis
```
### 4. 각 서비스 실행
실행방법
>방법1 : java -jar ${server}-0.0.1-SNAPSHOT.jar <br>
>방법2 : 1) plop/deploy 폴더에 jar 파일을 모아두고 2) serverStart.sh 실행

결과 확인 
>LINUX) netstat -anop | grep LISTEN <br>
>MAC) sudo lsof -PiTCP -sTCP:LISTEN <br>
>서비스 레지스트리로 확인 : localhost:8761 접속

서버 다운
>kill -9 pid 각각 수동으로 실행

### <ec2 off 시> MYSQL 설정
로컬 환경에서 실행하기 위해 MYSQL에 데이터베이스를 설정해주어야 함.
>1. 3306 mysql 실행 (AWS가 막힌 경우 로컬 혹은 도커) 
>2. plop database 생성
>3. user,friend table 생성
```sql
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userid` varchar(30) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(65) DEFAULT NULL,
  `profile` json DEFAULT NULL,
  `state` tinyint(1) DEFAULT NULL,
  `user_role` enum('ROLE_ADMIN','ROLE_USER') DEFAULT NULL,
  `device` json DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `access_at` datetime DEFAULT NULL,
  `login_at` datetime DEFAULT NULL,
  `fcm_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid` (`userid`),
  UNIQUE KEY `email` (`email`)
);

CREATE TABLE `friend` (
  `sender_id` bigint NOT NULL,
  `receiver_id` bigint NOT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`sender_id`,`receiver_id`)
);
```

### <ec2 off 시> mysql 관련 application.yml 수정
로컬 환경에서 실행하기 위해 application.yml 파일을 수정하여야함 <br>
applicaton.yml 폴더 위치 :<br> ```sh plop/src/server/${server}/src/main/resources/application.yml ``` <br>
mysql을 사용하는 서버 : auth,user,push -> 각 폴더의 application.yml 파일을 수정해야 함.
```yaml
---- 변경 전 ----
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://plop-rds.cyjccp4psnuz.ap-northeast-2.rds.amazonaws.com:3306/plop?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&serverTimezone=Asia/Seoul
    username: ${PLOP_DB_USER} # plop-db-user
    password: ${PLOP_DB_PWD} #plop-db-pwd
---- 변경 후 ----
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/plop?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&serverTimezone=Asia/Seoul
    username: ${PLOP_DB_USER} # plop-db-user
    password: ${PLOP_DB_PWD} #plop-db-pwd
```


