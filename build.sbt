val http4sVersion = "1.0.0-M19"

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization := "ch.ethz"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.5"

lazy val root = (project in file("."))
  .settings(
    name := "todo",
    publish := {},
    publishLocal := {}
  )
  .aggregate(todo.jvm, todo.js)

lazy val todo = crossProject(JSPlatform, JVMPlatform).in(file("."))
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-parser" % "0.14.0-M4",
      "io.circe" %%% "circe-generic" % "0.14.0-M4"
    )
  )
  .jvmSettings(
    reStart / mainClass := Some("ch.ethz.todo.Server"),
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-scalatags" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion
    )
  )
  .jsSettings(
    reStart / mainClass := None,
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "0.12.2",
      "com.raquo" %%% "airstream" % "0.12.2"
    )
  )
  .enablePlugins(ScalaJSPlugin)
