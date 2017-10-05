package com.github.ilittleangel.bigdata

import scala.collection.JavaConversions._

class AppConfig(config: com.typesafe.config.Config) extends Serializable {
  private val rootCfg = config.getConfig("configuration")

  lazy val brokers: String = rootCfg.getStringList("input.kafka.brokers").mkString(",")
  lazy val schemaRegistry: String = rootCfg.getString("input.kafka.schema_registry")
  lazy val topics: Set[String] = rootCfg.getStringList("input.kafka.topics").toSet
  lazy val batchDuration: Int = rootCfg.getInt("spark.configuration.batch_duration_in_seconds")
  lazy val checkpoint: String = rootCfg.getString("spark.configuration.checkpoint_dir")

}
