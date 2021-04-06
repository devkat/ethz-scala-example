package ch.ethz.todo.components

import scala.collection.immutable.Seq

import ch.ethz.todo.components.bootstrap.Checkbox
import ch.ethz.todo.components.bootstrap.ListGroup
import ch.ethz.todo.components.bootstrap.ListGroupItem
import ch.ethz.todo.domain._
import com.raquo.laminar.api.L._

object TodoList {

  def apply(tasksStream: EventStream[Seq[Task]]) =
    ListGroup(
      children <-- tasksStream.map(
        _.map(task =>
          ListGroupItem(
            Checkbox(task.label)
          )
        )
      )
    )

}
