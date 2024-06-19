### Rabbitmq 실행 명령어
- docker run -d --name rabbitmq --network ecommerce-network 
-p 15672:15672 -p 5672:5672 -p 15671:15671 -p 5671:5671 -p 4369:4369
-e RABBITMQ_DEFAULT_USER=guest
-e RABBITMQ_DEFAULT_PASS=guest rabbitmq:management


### Config-service 실행 명령어
- docker run -d -p 8888:8888 --network ecommerce-network 
-e "spring.rabbitmq.host=rabbitmq" 
-e "spring.profiles.active=default" 
--name config-service yongjung/config-service:1.0


### Discovery-service 실행 명령어
- docker run -d -p 8761:8761 --network ecommerce-network 
-e "spring.cloud.config.uri=http://config-service:8888"
--name discovery-service edowon0623/discovery-service:1.0


### Apigateway-service 실행 명령어
- docker run -d -p 8000:8000 --network ecommerce-network 
-e "spring.cloud.config.uri=http://config-service:8888" -e "spring.rabbitmq.host=rabbitmq"
-e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" 
--name apigateway-service yongjung/apigateway-service:1.0


### Kafka 실행 명령어
- docker-compose-single-broker.yml 파일이 있는 경로로 이동 후 
- docker-compose -f docker-compose-single-broker.yml up -d


### Zipkin 실행 명령어
- docker run -d -p 9411:9411 --network ecommerce-network --name zipkin openzipkin/zipkin


### Prometheus 실행 명령어
- docker run -d -p 9090:9090 --network ecommerce-network --name prometheus 
-v /Users/junge/IdeaProjects/inflearnSpringCloud/prometheus-2.53.0-rc.1.darwin-amd64/prometheus.yml:/etc/promethues/promethues.yml 
prom/prometheus


### Grafana 실행 명령어
- docker run -d -p 3000:3000 --network ecommerce-network --name grafana grafana/grafana


### User-service 실행 명령어
- docker run -d --network ecommerce-network --name user-service 
-e "spring.cloud.config.uri=http://config-service:8888" 
-e "spring.rabbitmq.host=rabbitmq" 
-e "management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans" 
-e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" 
-e "logging.file=/api-logs/users-ws.log" yongjung/user-service:1.0


### Order-service 실행 명령어
- docker run -d --network ecommerce-network --name order-service 
-e "spring.cloud.config.uri=http://config-service:8888"
-e "spring.rabbitmq.host=rabbitmq"
-e "spring.zipkin.base-url=http://zipkin:9411"
-e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" 
-e "spring.datasource.url=jdbc:mariadb://mariadb:3306/mydb"
-e "spring.kafka.consumer.bootstrapServers=172.18.0.101:9092"
-e "logging.file=/api-logs/users-ws.log" 
yongjung/order-service:1.0


### Catalog-service 실행 명령어
- docker run -d --network ecommerce-network --name catalog-service 
-e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" 
-e "spring.kafka.bootstrapServers=172.18.0.101:9092"
-e "spring.datasource.data=Data.sql"
-e "logging.file=/api-logs/catalog-ws/log" 
yongjung/catalog-service:1.0