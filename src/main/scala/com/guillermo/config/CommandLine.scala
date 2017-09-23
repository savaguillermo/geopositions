package com.guillermo.config

/**
  * Created by g.sanchez
  */
case class CommandLine(path: String = "", point: Point = Point(0, 0), radius: Double = 0.0)

case class Point(xCoordinate: Int, yCoordinate: Int)