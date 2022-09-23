package discourse_bot

import org.apache.commons.text.StringEscapeUtils
import scala.annotation.tailrec

final case class Topic(
  url: String,
  title: String,
  published: String,
  content: String
) {

  def id: String = url

  def tweetString: String = {
    Topic.resizeTweetString(url + " " + title + "\n\n" + formattedContent)
  }

  private[this] def formattedContent: String = {
    @tailrec
    def loop(str: String): String = {
      val replaced = Topic.trimMap.foldLeft(str) { case (s, (oldStr, newStr)) =>
        s.replaceAll(oldStr, newStr)
      }
      if (replaced != str) {
        loop(replaced)
      } else {
        replaced
      }
    }
    val result = loop(Topic.escape(StringEscapeUtils.unescapeXml(content).replaceAll("\\<[^>]*>", "")))
    result.lines.filterNot { s =>
      title.contains(s) || s.contains(title)
    }.mkString("\n")
  }
}

object Topic {
  private[this] val escapeMap = Map(
    "@" -> "🍥",
    "\\.md" -> "_md",
    "\\.sh" -> "_sh",
    "\\.py" -> "_py",
    "\\.java" -> "_java",
    "\\.Final" -> "_Final",
  )

  def escape(str: String): String =
    escapeMap.foldLeft(str) { case (s, (k, v)) => s.replaceAll(k, v) }

  private val trimMap: Map[String, String] = Map(
    ("\n\n\n", "\n\n"),
    ("  ", " "),
    (" \n", "\n"),
    ("\n ", "\n")
  )

  def apply(rawData: xml.Node): Topic = {
    Topic(
      (rawData \ "link").text.trim,
      (rawData \ "title").text.trim,
      (rawData \ "pubDate").text.trim,
      (rawData \ "description").text.trim.take(300)
    )
  }

  def isASCII(c: Char): Boolean = {
    (0x0 <= c) && (c <= 0x7f)
  }

  def resizeTweetString(tweet: String): String = {
    if (tweet.length <= 140) {
      tweet
    } else {
      val original = tweet.toCharArray
      val buf = new java.lang.StringBuilder()
      @annotation.tailrec
      def loop(i: Int, size: Int): Unit = {
        original.lift.apply(i) match {
          case Some(char) =>
            val nextSize = size + {
              if (isASCII(original(i))) {
                1
              } else {
                2
              }
            }
            if (nextSize > 280) {
              ()
            } else {
              buf.append(char)
              loop(i + 1, nextSize)
            }
          case None =>
            ()
        }
      }
      loop(0, 0)
      buf.toString
    }
  }
}
