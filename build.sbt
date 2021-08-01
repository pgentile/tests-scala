name := "tests-scala"
version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.0"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.8.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
libraryDependencies += "io.github.embeddedkafka" %% "embedded-kafka" % "2.8.0" % Test

val slf4jVersion = "1.7.32"
libraryDependencies += "org.slf4j" % "slf4j-api" % slf4jVersion
libraryDependencies += "org.slf4j" % "slf4j-simple" % slf4jVersion

idePackagePrefix := Some("org.example.testsscala")
