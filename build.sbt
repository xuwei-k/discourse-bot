name := "discourse-bot"

licenses := Seq("MIT License" -> url("https://www.opensource.org/licenses/mit-license"))

scalaVersion := "2.12.14"

val twitter4jVersion = "4.0.7"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.apache.commons" % "commons-text" % "1.9",
  "org.twitter4j" % "twitter4j-core" % twitter4jVersion,
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

val unusedWarnings = Seq("-Ywarn-unused")

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Xfuture",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions"
) ++ unusedWarnings

Seq(Compile, Test).flatMap(c => scalacOptions in (c, console) --= unusedWarnings)

assemblyJarName in assembly := {
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"${name.value}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"
}

resourceGenerators in Compile += task(
  Seq(baseDirectory.value / "build.sbt")
)

sourcesInBase := false
