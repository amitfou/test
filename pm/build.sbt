name := """pm"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.webjars" % "angularjs" % "1.3.11",
  "org.webjars" % "angular-ui-bootstrap" % "0.12.0",
  "org.mongodb" % "mongo-java-driver" % "2.12.4",
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.46.4"
)
