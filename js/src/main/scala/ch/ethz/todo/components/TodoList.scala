package ch.ethz.todo.components

import ch.ethz.todo.ErrorMessage
//import ch.ethz.todo.Command
//import ch.ethz.todo.Command._
import ch.ethz.bootstrap.Checkbox
import ch.ethz.bootstrap.ListGroup
import ch.ethz.bootstrap.ListGroupItem
import ch.ethz.todo.domain._
import com.raquo.laminar.api.L._
import ch.ethz.bootstrap.TextInput
import org.scalajs.dom
import ch.ethz.todo.Client

object TodoList {

  def apply(tasksVar: Var[Either[ErrorMessage, List[(Int, Task)]]]) = {

    val taskList = tasksVar.signal.map(_.getOrElse(Nil))

    List(
      h1("Tasks"),
      div(
        className := "input-group my-3",
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
        )
        /*
        button(
          className := "btn btn-outline-secondary",
          `type` := "button",
          onClick --> Observer[dom.MouseEvent](onNext = ev => dom.console.log(ev.screenX)),
          "Add"
        )
         */
      ),
      ListGroup(
        children <-- taskList.map(
          _
            .filterNot(_._2.completed)
            .map { case (id, task) =>
              ListGroupItem(
                Checkbox(
                  inContext { thisNode =>
                    val response = thisNode
                      .events(onChange)
                      .flatMap(_ => Client.updateTask(id, task.copy(completed = true)))
                      .flatMap(_ => Client.tasks)
                    response --> tasksVar.writer
                  }
                )(task.label)
              )
            }
        )
      ),
      h3(className := "mt-3", "Completed"),
      ListGroup(
        children <-- taskList.map(
          _
            .filter(_._2.completed)
            .map { case (_, task) =>
              ListGroupItem(
                Checkbox(disabled := true, checked := true)(task.label)
              )
            }
        )
      )
    )
  }

  private val onEnterPress =
    onKeyPress.filter(_.keyCode == dom.ext.KeyCode.Enter)

}
