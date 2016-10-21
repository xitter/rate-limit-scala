package com.xitter

import com.xitter.serializers.JsonSupport
import com.xitter.services.HotelService
import spray.routing.{HttpService, Route}

import scala.concurrent.ExecutionContext

/**
  * Created by vijay on 21/10/16.
  */
trait ApiResources extends HttpService with JsonSupport {

  implicit val executionContext: ExecutionContext

  val hotelService: HotelService

  def hotelRoutes: Route = pathPrefix("hotels") {
    path(Segment) { id =>
      get {
        complete(hotelService.getHotel(id))
      }
    }
  }
}
