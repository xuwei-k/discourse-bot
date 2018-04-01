package discourse_bot

import scala.util.control.Exception.allCatch
import java.io.File
import java.net.URL

object Main {

  def main(args: Array[String]): Unit = {
    val configFile = allCatch.opt(args.head).getOrElse("config.scala")
    run(new File(configFile))
  }

  def getTopics(rss: URL): List[Topic] =
    (scala.xml.XML.load(rss) \ "channel" \ "item").map { Topic.apply }.toList

  def run(configFile: File): Unit = {
    val env = Env.fromConfigFile(configFile)
    import env._
    import env.config._
    val firstData = getTopics(rss)
    println("first data")
    println(firstData)
    db.insert(firstData.map { _.id })
    tweet(env, firstData.take(firstTweetCount))
    loop(env)
  }

  def tweet(env: Env, data: Seq[Topic]): Unit = {
    data.reverseIterator.filter { env.config.filter }.foreach { e =>
      Thread.sleep(env.config.tweetInterval.toMillis)
      env.client.tweet(e)
    }
  }

  @annotation.tailrec
  def loop(env: Env): Unit = {
    import env._
    import config._
    try {
      Thread.sleep(interval.toMillis)
      val oldIds = db.selectAll
      val newData = getTopics(rss).filterNot { a =>
        oldIds.contains(a.id)
      }
      env.db.insert(newData.map { _.id })
      tweet(env, newData)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
    }
    loop(env.reload)
  }
}
