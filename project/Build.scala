import sbt._
import play.Project._

object ApplicationBuild extends Build {

	val appName = "ManalithWiki"
	val appVersion = "1.0-SNAPSHOT"

	val appDependencies = Seq(
		jdbc,
		anorm,
		cache,
		"com.typesafe.play" %% "play-slick" % "0.6.0.1",
		"org.pac4j" % "play-pac4j_scala" % "1.2.0",
		"org.pac4j" % "pac4j-oauth" % "1.5.0")

	val main = play.Project(appName, appVersion, appDependencies).settings(
		ebeanEnabled := false)

}
