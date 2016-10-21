package com.xitter.services

import com.xitter.entity.Hotel
import scala.io.Source

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by vijay on 21/10/16.
  */
class HotelService(implicit val executionContext: ExecutionContext) {

  var hotelsByCity = getHotelByCityFromCSV

  def getHotel(id: String): Future[Option[List[Hotel]]] = Future {
    Some(hotelsByCity(id))
  }

  private def getHotelByCityFromCSV: Map[String, List[Hotel]] = {
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
