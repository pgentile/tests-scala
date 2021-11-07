organization := "org.example"
name := "tests-scala"
version := "0.1"

scalaVersion := "2.13.6"

val catsVersion = "2.3.0"
val slf4jVersion = "1.7.32"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3"
libraryDependencies += "org.typelevel" %% "cats-core" % catsVersion
libraryDependencies += "org.typelevel" %% "cats-effect" % catsVersion
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.8.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
libraryDependencies += "io.github.embeddedkafka" %% "embedded-kafka" % "2.8.0" % Test

libraryDependencies += "org.slf4j" % "slf4j-api" % slf4jVersion
libraryDependencies += "org.slf4j" % "slf4j-simple" % slf4jVersion

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"

idePackagePrefix := Some("org.example.testsscala")

enablePlugins(JavaAppPackaging)
