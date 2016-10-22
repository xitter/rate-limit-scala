package com.xitter.services

import com.xitter.entity.Hotel
import scala.io.Source

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by vijay on 21/10/16.
  */

class HotelService(implicit val executionContext: ExecutionContext) {

  def getHotelsByCity(city: String, sortOrder: String): Future[List[Hotel]] = Future {
    hotelsByCityMap(city)
      .sortBy(_.price)
      .sortWith((a, b) => {
        if (sortOrder.equalsIgnoreCase("DESC"))
          a.price > b.price
        else
          a.price < b.price
      })
  }

  /*
  * Load cities from the CSV into a map
  * key- city , value - @Hotel
  * */
  private val hotelsByCityMap = {
    val content = Source.fromFile("src/main/resources/hoteldb.csv").getLines.map(_.split(","))
    val hotels = scala.collection.mutable.ListBuffer[Hotel]()
    content.foreach(
      (row) => {
        if (!row(0).equals("CITY")) {
          hotels += new Hotel(row(0), row(1), row(2), row(3).toInt)
        }
      }
    )
    hotels.toList.map(hotel => (hotel.city, hotel))
      .groupBy(_._1)
      .mapValues(_.map(_._2))
  }

}
