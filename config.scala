import java.net.URL
import discourse_bot._
import scala.concurrent.duration._

new Config{
  val rss = new URL("https://contributors.scala-lang.org/latest.rss")
  val interval = 3.minute

  val twitter = new TwitterSettings{
    val consumerKey       = ""
    val consumerSecret    = ""
    val accessToken       = ""
    val accessTokenSecret = ""
  }

  override val filter = { _ => true }
}
