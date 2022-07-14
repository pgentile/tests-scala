organization := "org.example"
name := "tests-scala"
version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.8.0"
libraryDependencies += "com.beachape" %% "enumeratum" % "1.7.0"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.0"

libraryDependencies += "co.fs2" %% "fs2-core" % "3.2.3"
libraryDependencies += "co.fs2" %% "fs2-io" % "3.2.3"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % Test
libraryDependencies += "io.github.embeddedkafka" %% "embedded-kafka" % "3.0.0" % Test

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.32"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.32"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.17"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.17" % Test

libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-amqp" % "3.0.4"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.17"

libraryDependencies += "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-json4s" % "0.12.0"

libraryDependencies += "io.circe" %% "circe-parser" % "0.14.1"
libraryDependencies += "io.circe" %% "circe-generic" % "0.14.1"

libraryDependencies += "com.vladkopanev" %% "cats-saga" % "1.0.0-RC2"

libraryDependencies += "io.kamon" %% "kamon-bundle" % "2.5.5"
libraryDependencies += "io.kamon" % "kanela-agent" % "1.0.14" % "provided"

idePackagePrefix.withRank(KeyRanks.Invisible) := Some("org.example.testsscala")

enablePlugins(JavaAppPackaging)

Compile / PB.targets := Seq(
  scalapb.gen(flatPackage = true) -> (Compile / sourceManaged).value / "scalapb"
)
