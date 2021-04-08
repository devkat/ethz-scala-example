package ch.ethz.todo

import ch.ethz.todo.domain.Task
import com.raquo.airstream.core.EventStream
import com.raquo.airstream.web.AjaxEventStream
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import org.scalajs.dom.ext.Ajax

object Client {

  def tasks: EventStream[Either[ErrorMessage, List[(Int, Task)]]] =
    AjaxEventStream
      .get("/api/tasks")
      .as[List[(Int, Task)]]

  def insertTask(task: Task): EventStream[Either[ErrorMessage, Task]] =
    AjaxEventStream
      .post(
        url = "/api/tasks",
        data = Ajax.InputData.str2ajax(task.asJson.noSpaces),
        headers = Map("Content-Type" -> "application/json")
      )
      .as[Task]

  def updateTask(id: Int, task: Task): EventStream[Either[ErrorMessage, Task]] =
    AjaxEventStream
      .put(
        url = s"/api/tasks/$id",
        data = Ajax.InputData.str2ajax(task.asJson.noSpaces),
        headers = Map("Content-Type" -> "application/json")
      )
      .as[Task]

  def deleteTask(id: Int): EventStream[Either[ErrorMessage, Unit]] =
    AjaxEventStream
      .delete(s"/api/tasks/$id")
      .as[Unit]

  private implicit class EventStreamSyntax(val stream: AjaxEventStream) {
    def as[B: Decoder]: EventStream[Either[ErrorMessage, B]] = {
      return stream
        .map(_.responseText)
        .map(decode[B])
        .map(_.left.map[String](_.getMessage()))
    }
  }

}
