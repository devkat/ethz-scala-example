package ch.ethz.todo.components

import cats.implicits._
import ch.ethz.todo.ErrorMessage
import ch.ethz.bootstrap.Checkbox
import ch.ethz.bootstrap.ListGroup
import ch.ethz.bootstrap.ListGroupItem
import ch.ethz.todo.domain._
import com.raquo.laminar.api.L._
import ch.ethz.bootstrap.TextInput
import org.scalajs.dom
import ch.ethz.todo.Client
import com.raquo.domtypes.jsdom.defs.events.TypedTargetEvent

object TodoList {

  def apply(tasksVar: Var[Either[ErrorMessage, List[(Int, Task)]]]) = {

    val taskList = tasksVar.signal.map(_.getOrElse(Nil))

    def tasksView(completed: Boolean) =
      ListGroup(
        children <-- taskList.map(
          _
            .filter(_._2.completed === completed)
            .map { case (id, task) =>
              ListGroupItem(
                Checkbox(
                  checked := task.completed,
                  inContext { thisNode =>
                    val response =
                      updateTask(
                        thisNode.events(onChange),
                        id,
                        task.copy(completed = !task.completed)
                      )
                    response --> tasksVar.writer
                  }
                )(task.label)
              )
            }
        )
      )

      /*
    val newTaskBus: EventBus[String] =
      new EventBus
       .flatMap(label => Client.insertTask(Task(label, false)))
        .flatMap(_ => Client.tasks)
        */

    List(
      h1("Tasks"),
      div(
        cls := "row",
        div(
          cls := "col-sm-6",
          div(
            cls := "input-group my-3",
            TextInput(
              placeholder := "New task",
              autoFocus(true),
              inContext { thisNode =>
                val response = thisNode
                  .events(onEnterPress)
                  .mapTo(thisNode.ref.value)
                  .filter(_.nonEmpty)
                  .flatMap(label => Client.insertTask(Task(label, false)))
                  .flatMap(_ => Client.tasks)
                response --> { r =>
                  thisNode.ref.value = ""
                  tasksVar.set(r)
                }
              }
            ),
            button(
              cls := "btn btn-outline-secondary",
              `type` := "button",
              onClick --> Observer[dom.MouseEvent](onNext = ev => dom.console.log(ev.screenX)),
              "Add"
            )
          )
        )
      ),
      div(
        cls := "row",
        div(
          cls := "col-md-6",
          h3(className := "mt-3", "Open"),
          tasksView(false)
        ),
        div(
          cls := "col-md-6",
          h3(cls := "mt-3", "Completed"),
          tasksView(true)
        )
      )
    )
  }

  private val onEnterPress =
    onKeyPress.filter(_.keyCode == dom.ext.KeyCode.Enter)

  private def updateTask(
      stream: EventStream[TypedTargetEvent[dom.Element]],
      id: Int,
      task: Task
  ) =
    stream
      .flatMap(_ => Client.updateTask(id, task))
      .flatMap(_ => Client.tasks)

}
