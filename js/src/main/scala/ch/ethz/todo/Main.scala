package ch.ethz.todo

import org.scalajs.dom
import ch.ethz.todo.components.TodoList
import com.raquo.airstream.core.Signal
import scala.collection.immutable.Seq
import ch.ethz.todo.domain.Task
import com.raquo.airstream.web.AjaxEventStream
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import com.raquo.airstream.core.EventStream
import com.raquo.laminar.api.L._

object Main {

  implicit val decoder: Decoder[Task] = Task.decoder

  val taskStream: EventStream[Either[ErrorMessage, Seq[Task]]] =
    AjaxEventStream
      .get("/api/tasks")
      .map(_.responseText)
      .map(decode[Seq[Task]])
      .map(_.left.map[String](_.getMessage()))

  def init = {
    val appContainer: dom.Element = dom.document.getElementById("app")
    val appElement = div(
      child <-- taskStream.map(_.fold(div("Error: ", _), _ => div())),
      TodoList(taskStream.map(_.getOrElse(Seq.empty)))
    )
    render(appContainer, appElement)
  }

  def main(args: Array[String]): Unit = {
    dom.document.addEventListener("DOMContentLoaded", (e: dom.Event) => init)
  }

}
