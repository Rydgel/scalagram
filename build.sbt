name := "scalagram"

scalaVersion := "2.11.4"

scalacOptions += "-feature"

resolvers ++= Seq(
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.3.7",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)