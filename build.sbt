name := "ScalaDelRay"

version := "1.1"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-feature", "-language:postfixOps")

mainClass in (Compile,run) := Some("scaladelray.ui.ScalaDelRay")

packageOptions  += Package.MainClass("scaladelray.ui.ScalaDelRay")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11+"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.9"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.3.9"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.2"