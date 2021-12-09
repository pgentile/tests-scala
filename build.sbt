organization := "org.example"
name := "tests-scala"
version := "0.1"

scalaVersion := "2.13.7"

val slf4jVersion = "1.7.32"
val akkaVersion = "2.6.17"

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.6.1"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.8"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.8.0"
libraryDependencies += "com.beachape" %% "enumeratum" % "1.7.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % Test
libraryDependencies += "io.github.embeddedkafka" %% "embedded-kafka" % "3.0.0" % Test

libraryDependencies += "org.slf4j" % "slf4j-api" % slf4jVersion
libraryDependencies += "org.slf4j" % "slf4j-simple" % slf4jVersion

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test

libraryDependencies += "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-json4s" % "0.12.0"

idePackagePrefix.withRank(KeyRanks.Invisible) := Some("org.example.testsscala")

enablePlugins(JavaAppPackaging)

Compile / PB.targets := Seq(
  scalapb.gen(flatPackage = true) -> (Compile / sourceManaged).value / "scalapb"
)
