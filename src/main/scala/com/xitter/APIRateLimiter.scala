package com.xitter

import java.util

import com.typesafe.config.ConfigFactory


/**
  * Created by vijay on 21/10/16.
  */
trait APIRateLimiter {

  val config = ConfigFactory.load()

  val defaultAPIRate = (config.getDoubleList("globalRate").get(0), config.getDoubleList("globalRate").get(1))

  val customAPIKeyRate = config.getObject("customAPIKeyRate").unwrapped()

  var apiKeyAllowance = Map[String, Double]()
  var apiKeyLastTime = Map[String, Int]()

  /*
  * Following is Token Bucket Algorithm for rate limiting
  * https://en.wikipedia.org/wiki/Token_bucket
  * http://stackoverflow.com/questions/667508/whats-a-good-rate-limiting-algorithm
  * */
  def validAPIRateLimit(key: String): Boolean = {

    var rate = defaultAPIRate._1
    var interval = defaultAPIRate._2

    if (customAPIKeyRate containsKey key) {
      //Excuse for complex typecasts, would find a better way
      val list = customAPIKeyRate.get(key).asInstanceOf[util.ArrayList[Double]]
      rate = list.get(0).asInstanceOf[Integer].doubleValue()
      interval = list.get(1).asInstanceOf[Integer].doubleValue()
    }

    if (!(apiKeyAllowance contains key)) {
      apiKeyAllowance += (key -> rate)
      apiKeyLastTime += (key -> (System.currentTimeMillis / 1000).toInt)
    }

    val current = (System.currentTimeMillis / 1000).toInt
    val timePassed = current - apiKeyLastTime(key)

    apiKeyLastTime += (key -> current)

    var allowance = apiKeyAllowance(key) + timePassed * (rate / interval)

    if (allowance > rate) {
      allowance = rate // throttle
    }

    if (allowance < 1.0) {
      apiKeyAllowance += (key -> allowance)
      false
    } else {
      allowance -= 1.0
      apiKeyAllowance += (key -> allowance)
      true
    }

  }
}
