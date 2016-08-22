name := "grid-dynamics-exercise"
version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation")

enablePlugins(JavaAppPackaging)

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.9-RC2"
libraryDependencies += "com.typesafe.akka" %% "akka-contrib" % "2.4.9-RC2"
libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.9-RC2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
