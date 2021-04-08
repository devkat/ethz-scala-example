val http4sVersion = "0.22.0-M6" //"1.0.0-M19"

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  List(
    scalaVersion := "2.13.5",
    version := "0.1.0-SNAPSHOT",
    organization := "ch.ethz",
    scalacOptions ++= Seq("-Ywarn-unused"),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"
  )
)

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
      "io.circe" %%% "circe-parser"  % "0.14.0-M4",
      "io.circe" %%% "circe-generic" % "0.14.0-M4"
    )
  )
  .jvmSettings(
    reStart / mainClass := Some("ch.ethz.todo.Server"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"           % "2.5.0",
      "org.typelevel" %% "cats-effect"         % "2.4.1",
      "co.fs2"        %% "fs2-core"            % "2.5.3",
      "org.http4s"    %% "http4s-dsl"          % http4sVersion,
      "org.http4s"    %% "http4s-blaze-server" % http4sVersion,
      "org.http4s"    %% "http4s-scalatags"    % http4sVersion,
      "org.http4s"    %% "http4s-circe"        % http4sVersion,
      "org.tpolecat"  %% "doobie-core"         % "0.12.1",
      "org.tpolecat"  %% "doobie-h2"           % "0.12.1",
      "com.h2database" % "h2"                  % "1.4.197",
      "org.slf4j"      % "slf4j-simple"        % "1.7.30"
    )
  )
  .jsSettings(
    reStart / mainClass := None,
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar"   % "0.12.2",
      "com.raquo" %%% "airstream" % "0.12.2"
    )
  )
  .enablePlugins(ScalaJSPlugin)
