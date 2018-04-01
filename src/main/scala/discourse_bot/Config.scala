package discourse_bot

import java.net.URL

import scala.concurrent.duration._

abstract class Config {
  val twitter: TwitterSettings
  val rss: URL
  val interval: Duration
  val dbSize: Int = 100
  val filter: Topic => Boolean = { _ =>
    true
  }
  val tweetInterval: Duration = 1000.millis
  val firstTweetCount: Int = 1
}

abstract class TwitterSettings {
  val consumerKey: String
  val consumerSecret: String
  val accessToken: String
  val accessTokenSecret: String
}
