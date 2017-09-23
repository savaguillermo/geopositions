package com.guillermo

import com.guillermo.config.{CommandLine, Point}
import org.apache.spark.SparkContext
import org.slf4j.LoggerFactory


object GeoblinkJob extends Serializable {

  @transient lazy private val Log = LoggerFactory.getLogger(getClass.getName)

  val COLUMN_TO_CALCULE = 7

  def run(config: CommandLine, sc: SparkContext): Unit = {

    //for this small example (small file) it is not necessary partition the file

    //read CSV file from path
    val readCSV = sc.textFile(config.path)

    //could be use a CSV API to drop header such as SparkCSV
    val withoutHeader = readCSV.mapPartitionsWithIndex { (idx, iter) => if (idx == 0) iter.drop(1) else iter }

    val splitRDD = withoutHeader.map(x => x.split(','))

    //to avoid IndexOutOfBOund
    require(COLUMN_TO_CALCULE >= 0)

    val parsePoints = splitRDD.map(x => (createPointFromLine(x), x(COLUMN_TO_CALCULE + 1).toInt))

    //filter point inside circle
    val insideCircle = parsePoints.filter(x => pointInCircle(config.point, x._1, config.radius))

    //# points inside circle
    val countInside = insideCircle.count()

    //sum and average
    val sum = insideCircle.values.sum()
    val averge = sum / countInside

    //median
    val sorted = insideCircle.map(x => x._2).sortBy(identity).zipWithIndex().map {
      case (v, idx) => (idx, v)
    }

    val median: Double = if (countInside % 2 == 0) {
      val l = countInside / 2 - 1
      val r = l + 1
      (sorted.lookup(l).head + sorted.lookup(r).head).toDouble / 2
    } else sorted.lookup(countInside / 2).head.toDouble

    println("These are the results:")
    println("-There are " + countInside + " points inside the radius")
    if (countInside > 1) {
      println("-Sum: " + sum)
      println("-Average: " + averge)
      println("-Median: " + median)
    }
    println("------------------")


  }

  /**
    * Calcules if a point given  is inside a radius
    *
    * @param origin Origin point
    * @param point  Point to calcule
    * @param radius radius units
    */
  def pointInCircle(origin: Point, point: Point, radius: Double): Boolean = {

    val distance = Math.sqrt(Math.pow((point.xCoordinate - origin.xCoordinate), 2) + Math.pow((point.yCoordinate - origin.yCoordinate), 2))
    if (distance > radius) {
      false
    } else {
      true
    }
  }

  /**
    * Create a Point from line array
    *
    * @param line split line in array format
    */
  def createPointFromLine(line: Array[String]): Point = {
    Point(line(0).toInt, line(1).toInt)
  }
}