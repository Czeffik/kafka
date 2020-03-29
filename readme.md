#Kafka sample app
App consume from one kafka topic and publish to other kafka topic.
Message consumed from first topic before publishing on other should be translated by calling to external service
for required information (not implemented yet - currently return fixed description)
###Build
```shell script
./gradlew clean build
```
###Setup environment
Start docker compose:
```shell script
docker-compose up
```
When started - open bash:
```shell script
docker exec -it kafka /bin/bash
```

Go to:
```shell script
cd /opt/bitnami/kafka/bin/
```

Create topics:
```shell script
./kafka-topics.sh --create --zookeeper zookeeper:2181 --topic information --partitions 4 --replication-factor 1
./kafka-topics.sh --create --zookeeper zookeeper:2181 --topic information-translated --partitions 4 --replication-factor 1
```
where `information` and `information-translated` are topic names

####Run application:
Select one of:
- run `main` method in `com.trzewik.kafka.App` from intellij
- run `./gradlew bootRun`
- build application with `./gradlew clean build` and execute: `java -jar build/libs/kafka-*.jar`


####Send sample message:
Open producer console:
```shell script
./kafka-console-producer.sh --broker-list kafka:29092 --topic information --property "parse.key=true" --property "key.separator=:"
```
Example `Information` message:
```shell script
sample_key:{"name": "Example name", "description": "Not translated description"}
other_sample_key:{"name": "Example name", "description": "Not translated description"}
```
