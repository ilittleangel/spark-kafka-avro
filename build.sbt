val metaSettings = Seq(
  name := "spark-kafka-avro",
  description := "POC: Spark consumer for Kafka Avro messages",
  version := "1.0.0"
)

val scalaSettings = Seq(
  scalaVersion := "2.10.6",
  scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")
)

val deploymentSettings = Seq(
  mainClass in assembly := Some("io.scalac.spark.AvroConsumer"),
  assemblyOutputPath in assembly := file("target/deploy/spark-kafka-avro.jar"),
  test in assembly := {}
)

val repositories = Seq(
  "confluent" at "http://packages.confluent.io/maven/",
  Resolver.sonatypeRepo("public")
)

val dependencies = Seq(
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.0"  % "compile",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.0"
    exclude("org.spark-project.spark", "unused"),
  "org.apache.avro" % "avro" % "1.7.7",
  "io.confluent" % "kafka-avro-serializer" % "1.0",
  "com.github.scopt" %% "scopt" % "3.3.0",
  "joda-time" % "joda-time" % "2.8.1"
)

lazy val root = (project in file(".")).
  settings(metaSettings: _*).
  settings(scalaSettings: _*).
  settings(deploymentSettings: _*).
  settings(resolvers ++= repositories).
  settings(libraryDependencies ++= dependencies)
