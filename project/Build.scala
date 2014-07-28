import play.Play.autoImport._
import sbt.Keys._
import sbt._

object ApplicationBuild extends Build {

	val appName = "ManalithWiki"
	val appVersion = "1.0-SNAPSHOT"
	val appScalaVersion = "2.11.1"

	val appDependencies = Seq(
		jdbc,
		anorm,
		cache,
		ws,
		"com.typesafe.play" %% "play-slick" % "0.8.0-RC2",
		"org.pac4j" % "play-pac4j_scala" % "1.3.0-SNAPSHOT",
		"org.pac4j" % "pac4j-oauth" % "1.6.0-SNAPSHOT")

	val appResolvers = Seq(
		"Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
	)

	val main = Project(appName, file(".")).enablePlugins(play.PlayScala).settings(
		version := appVersion,
		resolvers ++= appResolvers,
		libraryDependencies ++= appDependencies,
		scalaVersion := appScalaVersion)

}
