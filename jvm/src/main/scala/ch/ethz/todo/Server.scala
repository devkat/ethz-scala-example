package ch.ethz.todo

import cats.effect._
import cats.implicits._
import ch.ethz.todo.db.Database
import ch.ethz.todo.domain.Task
import ch.ethz.todo.rest.Service
import ch.ethz.todo.view.Home
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.scalatags._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.staticcontent._

import scala.concurrent.ExecutionContext.global

object Server extends IOApp {

  def todoService(service: Service[IO]): HttpRoutes[IO] =
    HttpRoutes.of[IO] {

      case GET -> Root =>
        Ok(Home.page)

      case GET -> Root / "api" / "tasks" =>
        service.tasks.flatMap(Ok(_))

      case req @ POST -> Root / "api" / "tasks" =>
        req.as[Task].flatMap(service.insertTask).flatMap(Ok(_))

      case req @ PUT -> Root / "api" / "tasks" / IntVar(id) =>
        req.as[Task].flatMap(service.updateTask(id, _)).flatMap(Ok(_))

      case DELETE -> Root / "api" / "tasks" / IntVar(id) =>
        service.deleteTask(id).flatMap(Ok(_))
    }

  def httpApp(blocker: Blocker, service: Service[IO]): HttpApp[IO] =
    Router[IO](
      "/scripts" -> fileService[IO](FileService.Config(
        "../js/target/scala-2.13/todo-fastopt",
        blocker
      )),
      "/" -> todoService(service)
    ).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    Tuple2
      .apply(
        Database.transactor,
        Blocker[IO]
      )
      .tupled
      .flatMap { case (xa, blocker) =>
        BlazeServerBuilder[IO](global)
          .bindHttp(8080, "localhost")
          .withHttpApp(httpApp(blocker, Service.ioInstance(xa)))
          .resource.map((xa, _))
      }
      .use { case (xa, server) =>
        Database.init(xa) *>
          IO(println(s"Started server at ${server.baseUri}")) *>
          IO.never
      }
      .as(ExitCode.Success)

}
