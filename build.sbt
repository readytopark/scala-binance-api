ThisBuild / organization := "io.github.patceev"
ThisBuild / organizationName := "github"
ThisBuild / organizationHomepage := Some(url("https://github.io"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/patceev/scala-binance-api"),
    "scm:git@github.com:patceev/scala-binance-api.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "patceev",
    name = "Patceev Andrey",
    email = "patceev@protonmail.com",
    url = url("https://patceev.github.io")
  )
)

ThisBuild / description := "Asynchronous & Easy to use wrapper for Binance API"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/patceev/scala-binance-api"))

ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

name := "scala-binance-api"

version := "0.0.2-SNAPSHOT"
organization := "io.github.patceev"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.21",
  "com.typesafe.akka" %% "akka-http" % "10.1.7",
  "com.typesafe.akka" %% "akka-stream" % "2.5.21",
  "io.circe" %% "circe-core" % "0.10.0",
  "io.circe" %% "circe-generic" % "0.10.0",
  "io.circe" %% "circe-parser" % "0.10.0",
  "com.beachape" %% "enumeratum-circe" % "1.5.10",
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",
  "com.typesafe" % "config" % "1.3.4"
)
