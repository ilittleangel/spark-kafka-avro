package com.github.ilittleangel.bigdata

import com.typesafe.config.ConfigFactory
import io.confluent.kafka.serializers.KafkaAvroDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object AvroKafkaConsumer {

  final val AppName = "Spark Kafka Avro"

  def createContextFactory(cfg: AppConfig): () => StreamingContext = { () =>

      println("Creating new context")

      val sparkConf = new SparkConf()
        .setMaster("local[*]")
        .setAppName(AppName)
        .set("", "")
      val ssc = new StreamingContext(sparkConf, Seconds(cfg.batchDuration))

      val kafkaParams = Map[String, String](
        "auto.offset.reset" -> "smallest",
        "metadata.broker.list" -> cfg.brokers,
        "schema.registry.url" -> cfg.schemaRegistry)

      val messages = KafkaUtils.createDirectStream[Object, Object, KafkaAvroDecoder, KafkaAvroDecoder](ssc, kafkaParams, cfg.topics)
      messages.map(_._2.toString).print

      ssc.checkpoint(cfg.checkpoint)
      ssc
  }

  def main(args: Array[String]) {

    AppArgumentParser.parse(args, AppArguments()) match {
      case None => sys.exit(1)
      case Some(parsedArgs) =>
        val config = new AppConfig(ConfigFactory.parseFile(parsedArgs.conf))
        val createContext = createContextFactory(config)
        val ssc = StreamingContext.getActiveOrCreate(config.checkpoint, createContext)
        sys.addShutdownHook {
          ssc.stop(stopSparkContext = true, stopGracefully = true)
        }
        ssc.start()
        ssc.awaitTermination()
    }
  }
}
