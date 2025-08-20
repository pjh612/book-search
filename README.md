# book-search
TREVARI 사전과제

# 실행 방법

make file을 실행시켜 Spring Boot 애플리케이션을 빌드해 도커 이미지를 생성하고, docker-compose를 실행 시킵니다.
```bash
 make
```

### Docker-Compose 구성

- kafka cluster(KRAFT)
- Redis
- mysql
- spring boot application
  - book-api
  - book-web
  - keyword-consumer-service
- grafana
- prometheus
- kafka-exporter
- elasticsearch
- kafka-connect

### Grafana를 이용한 모니터링

Grafana에 접속
```
localhost:3000
```

Dashboard or Explore 확인
- 카프카 모니터링
  - Dashboards -> Kafka Exporter Overview
- Spring Boot Application 모니터링
  - Dashboards -> Spring Boot 3.x Statistics
- 로그 조회
  - Explore -> Query type -> Logs

