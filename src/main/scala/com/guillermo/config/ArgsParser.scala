package com.guillermo.config

import scopt.OptionParser

object ArgsParser extends OptionParser[Args]("ValidationDriver") {

  head(" Geoblink APP", "1.0")

  opt[String]('f', "filename") required() valueName "<filename>" action { (value, config) =>
    config.copy(filename = value) } text "CSV Path File "
  opt[Int]('x', "xCoordenate") required() valueName "<xCoordenate>" action { (value, config) =>
    config.copy(xCoordinate = value) } text "X coordinate point "
  opt[Int]('y', "yCoordenate") required() valueName "<yCoordenate>" action { (value, config) =>
    config.copy(yCoordinate = value) } text "Y coordinate point "
  opt[Double]('r', "radius") required() valueName "<radius>" action { (value, config) =>
    config.copy(radius = value) } text "Value radius to calcule with at least one decimal "

  help("help") text "Show this help"

  /**
    * Returns None when value is null
    *
    * @param value  value to be checked
    * @return
    */
  private def noneIfNullValue(value: String): Option[String] =
    Option(value).flatMap(str => if (str.equalsIgnoreCase("null")) None else Some(str))

}
case class Args(filename: String = "", xCoordinate: Int=0, yCoordinate: Int=0,  radius: Double = 0.0)
