package ch.ethz

import cats.effect._
import ch.ethz.rest.Service
import ch.ethz.view.Home
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.scalatags._
import org.http4s.circe.CirceEntityEncoder._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  private val helloWorldService =
    HttpRoutes.of[IO] {
      case GET -> Root / name => Ok(Home(name))
      case GET -> Root / "api" / "users" / IntVar(id) ~ "json" => Ok(Service.user(id))
    }

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(helloWorldService.orNotFound)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)

}