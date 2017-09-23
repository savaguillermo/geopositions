package com.guillermo

import com.guillermo.config.{Args, ArgsParser, CommandLine, Point}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory


/**
  * Created by g.sanchez
  */
object GeoblinkDriver {

  @transient lazy private val Log = LoggerFactory.getLogger(getClass.getName)

  /**
    * Spark Job Name
    */
  val SPARK_APP_NAME = "Geoblink APP"


  /** @param args Arguments
    *             -f : CSV path file
    *             -x : X coordenate
    *             -y : Y coordenate
    *             -r : Value radius to calcule
    */

  def main(args: Array[String]) {


    val sparkConf = new SparkConf().setAppName(SPARK_APP_NAME).setMaster("local")
    val sparkContext = new SparkContext(sparkConf)

    Log.info("Start Geoblink App")

    sparkContext.setLogLevel("ERROR")
    ArgsParser.parse(args, Args()).fold(ifEmpty = sys.exit(1)) { parsedArgs =>

      val conf = CommandLine(parsedArgs.filename, Point(parsedArgs.xCoordinate, parsedArgs.yCoordinate), parsedArgs.radius)

      GeoblinkJob.run(conf, sparkContext)
      sparkContext.stop()

    }


  }


}
