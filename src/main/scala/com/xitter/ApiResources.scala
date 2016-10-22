package com.xitter

import com.xitter.serializers.JsonSupport
import com.xitter.services.HotelService
import spray.routing.{HttpService, Route, ValidationRejection}

import scala.concurrent.ExecutionContext

/**
  * Created by vijay on 21/10/16.
  */
trait ApiResources extends HttpService with JsonSupport with APIRateLimiter {

  implicit val executionContext: ExecutionContext

  val hotelService: HotelService

  def validateApiKey(route: Route) =
    headerValueByName("api-key") { key =>
      if (validAPIRateLimit(key))
        route
      else
        reject(ValidationRejection("API Rate limit exceeded"))
    }

  def hotelRoutes: Route = pathPrefix("hotels") {
    path(Segment) { id =>
      get {
        validateApiKey {
          complete(hotelService.getHotel(id))
        }
      }
    }
  }
}
