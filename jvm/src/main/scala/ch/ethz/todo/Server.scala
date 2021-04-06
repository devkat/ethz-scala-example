package ch.ethz.todo

import cats.effect._
import ch.ethz.todo.rest.Service
import ch.ethz.todo.view.Home
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.scalatags._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.server.staticcontent._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext.global
import org.http4s.server.Router

object Server extends IOApp {

  val helloWorldService =
    HttpRoutes.of[IO] {
      case GET -> Root => Ok(Home.page)
      case GET -> Root / "api" / "tasks" => Ok(Service.tasks)
    }

  val httpApp: HttpApp[IO] =
    Router(
      "/scripts" -> fileService[IO](FileService.Config("../js/target/scala-2.13/todo-fastopt")),
      "/" -> helloWorldService
    ).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .resource
      .use(server => IO(println(s"Started server at ${server.baseUri}")) *> IO.never)
      .as(ExitCode.Success)

}