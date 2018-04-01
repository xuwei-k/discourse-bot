package discourse_bot

import java.net.URL

import org.junit.Test
import org.junit.Assert._

class DiscourseBotTest {
  @Test
  def escape(): Unit = {
    assertEquals(Topic.escape("README.md @ foo.java"), "README_md 🍥 foo_java")
  }

  @Test
  def printExamples(): Unit = {
    Main.getTopics(new URL("https://contributors.scala-lang.org/latest.rss")).map(_.tweetString).foreach { s =>
      println("-" * 100)
      println(s)
    }
  }
}
