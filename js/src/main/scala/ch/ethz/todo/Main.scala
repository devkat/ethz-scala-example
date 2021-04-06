package ch.ethz.todo

import scala.collection.immutable.Seq

import ch.ethz.todo.components.TodoList
import ch.ethz.todo.domain.Task
import com.raquo.airstream.core.EventStream

import com.raquo.airstream.web.AjaxEventStream
import com.raquo.laminar.api.L._

import io.circe.generic.auto._
import io.circe.parser._

import org.scalajs.dom

object Main {

  val taskStream: EventStream[Either[ErrorMessage, Seq[Task]]] =
    AjaxEventStream
      .get("/api/tasks")
      .map(_.responseText)
      .map(decode[Seq[Task]])
      .map(_.left.map[String](_.getMessage()))

  def init = {
    val appContainer: dom.Element = dom.document.getElementById("app")
    val appElement = div(
      className := "content",
      child <-- taskStream.map(_.fold(div("Error: ", _), _ => div())),
      TodoList(taskStream.map(_.getOrElse(Seq.empty)))
    )
    render(appContainer, appElement)
  }

  def main(args: Array[String]): Unit = {
    dom.document.addEventListener("DOMContentLoaded", (_: dom.Event) => init)
  }

}
