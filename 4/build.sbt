scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
