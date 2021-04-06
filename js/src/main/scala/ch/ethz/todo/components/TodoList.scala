package ch.ethz.todo.components

import ch.ethz.todo.domain._

import com.raquo.laminar.api.L._
import scala.collection.immutable.Seq

object TodoList {

  def apply(tasks: EventStream[Seq[Task]]) =
  div(
    className := "content",
    ol(
      children <-- tasks.map(_.map(_.label).map(li(_)))
    )
  )

}
