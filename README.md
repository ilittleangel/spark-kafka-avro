# Spark Streaming Kafka and Avro

## Introduction

Simple Spark Streaming application to consume Avro messages from Kafka topic.

## Quickstart

Download [Confluent CLI](https://www.confluent.io/download/)

Export `$CONFLUENT_HOME`

```bash
export CONFLUENT_HOME=/path/to/confluent-3.3.0
export PATH=$CONFLUENT_HOME/bin:$PATH
```

Start Kafka, Zookeeper and Schema Registry:

```bash
confluent start schema-registry
```

Send Avro records in JSON as the message value (make sure there is no space in the schema string):

```bash
kafka-avro-console-producer \
  --broker-list localhost:9092 \
  --topic mediacion.dns2 \
  --property parse.key=true \
  --property key.separator=, \
  --property key.schema='{"type":"string"}' \
  --property value.schema='{"type":"record","name":"myrecord","fields":[{"name":"f1","type":"string"}]}'
```

```bash
"key1",{"f1": "value1"}
"key2",{"f1": "value2"}
"key3",{"f1": "value3"}
```

Create and set correctly the configuration file to pass it as an input argument:

```bash
vi application.conf
```

* `brokers` is a list separated by comma
* `topics` is a list as well
* `schema_registry` is a string with full uri of Schema Registry

```yml
configuration {
  input {
    kafka {
      brokers: ["localhost:9092"]
      schema_registry: "http://localhost:8081"
      topics: ["mediacion.dns2"]
    }
  }
  spark {
    configuration {
      batch_duration_in_seconds: 10
    }
  }
}
```

Run Spark application with `application.conf` file as an argument and consume the messages:

```bash
sbt "run --config application.conf"
```

Yo should get in console messages like these:

```

-------------------------------------------
Time: 1507199500000 ms
-------------------------------------------
{"f1": "value1"}
{"f1": "value2"}
{"f1": "value3"}
```

## More about Confluent CLI and Schema Registry

Confluent CLI allow us to get up and running the full Confluent platform quickly on a single server.
It can start/stop all services such as Zookeeper, Kafka, Schema Registry, Connect and Kafka-Rest services in a very fast way.

The purpouse of Confluent CLI is just for development. It is running only in localhost server.

Some of CLI `confluent` commands are:

```bash
confluent help
confluent list
confluent help start
confluent start kafka
confluent start schema-registry
confluent start connect
confluent stop connect
confluent status
confluent current
open `confluent current`
confluent log kafka
confluent log zookeeper
confluent log connect
confluent log kafka -f
ll `confluent current`
confluent destroy
```

Check all subjects:

```bash
curl -X GET http://localhost:8081/subjects
```

Check versions of subject `mediacion.dn2-key`:

```bash
curl -X GET http://localhost:8081/subjects/mediacion.dns2-key/versions
```

More details about Schema Registry in [docs.confluent.io](https://docs.confluent.io/current/schema-registry/docs/serializer-formatter.html)
