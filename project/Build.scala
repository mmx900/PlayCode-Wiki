import sbt._
import play.Project._

object ApplicationBuild extends Build {

	val appName = "ManalithWiki"
	val appVersion = "1.0-SNAPSHOT"

	val appDependencies = Seq(
		// Add your project dependencies here,
		jdbc,
		anorm,
		cache,
		"com.typesafe.play" %% "play-slick" % "0.6.0.1")

	val main = play.Project(appName, appVersion, appDependencies).settings(
		// Add your own project settings here
		ebeanEnabled := false)

}
