import SonatypeKeys._

// Import default settings. This changes `publishTo` settings to use the Sonatype repository and add several commands for publishing.
sonatypeSettings

name := "scalagram"

version := "0.1.3"

scalaVersion := "2.11.4"

scalacOptions += "-feature"

resolvers ++= Seq(
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.3.7",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

organizationName := "com.github.Rydgel"

// Your project orgnization (package name)
organization := "com.github.Rydgel"

profileName := "com.github.Rydgel"

organizationHomepage := Some(url("http://phollow.fr"))

description := "Instagram API wrapper for Scala"

pomExtra :=
  <url>https://github.com/Rydgel/scalagram</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>http://www.opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:Rydgel/scalagram.git</url>
    <connection>scm:git:git@github.com:Rydgel/scalagram.git</connection>
  </scm>
  <developers>
    <developer>
      <id>rydgel</id>
      <name>Jérôme Mahuet</name>
      <url>http://phollow.fr</url>
    </developer>
  </developers>
