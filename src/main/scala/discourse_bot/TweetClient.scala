package discourse_bot

import twitter4j._
import twitter4j.conf._

final case class TweetClient(conf: TwitterSettings) {

  private[this] val t = {
    val c = new ConfigurationBuilder
    c.setDebugEnabled(true)
      .setOAuthConsumerKey(conf.consumerKey)
      .setOAuthConsumerSecret(conf.consumerSecret)
      .setOAuthAccessToken(conf.accessToken)
      .setOAuthAccessTokenSecret(conf.accessTokenSecret)

    new TwitterFactory(c.build()).getInstance()
  }

  def tweet(a: Topic): Unit = {
    allCatchPrintStackTrace {
      try {
        val tweet = a.tweetString
        t.updateStatus(tweet)
      } catch {
        case e: Throwable =>
          println(e)
          t.updateStatus(a.tweetString.take(140))
      }
    }
  }

}
