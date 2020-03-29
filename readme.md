#Kafka sample app

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
