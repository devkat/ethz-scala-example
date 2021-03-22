val http4sVersion = "1.0.0-M19"

lazy val root = (project in file("."))
  .settings(
    organization := "ch.ethz",
    name := "scala-example",
    scalaVersion := "2.13.5",
    libraryDependencies := Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-scalatags" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % "0.14.0-M4"
    )
  )
