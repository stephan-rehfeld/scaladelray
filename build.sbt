name := "ScalaDelRay"

version := "0.9-beta"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-feature", "-language:postfixOps")

mainClass in (Compile,run) := Some("scaladelray.ui.ScalaDelRay")

packageOptions  += Package.MainClass("scaladelray.ui.ScalaDelRay")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.2"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.1"

libraryDependencies += "com.typesafe" % "config" % "1.0.2"