package com.xitter

import com.xitter.services.HotelService
import spray.routing.{HttpServiceActor, Route}
import scala.concurrent.ExecutionContext

/**
  * Created by vijay on 21/10/16.
  */
class RestInterface(implicit val executionContext: ExecutionContext) extends HttpServiceActor with ApiResources {

  val hotelService = new HotelService

  val routes: Route = hotelRoutes

  def receive = runRoute(routes)
}
