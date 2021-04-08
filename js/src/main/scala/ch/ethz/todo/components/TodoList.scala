package ch.ethz.todo.components

import cats.implicits._
import ch.ethz.bootstrap.Checkbox
import ch.ethz.bootstrap.ListGroup
import ch.ethz.bootstrap.ListGroupItem
import ch.ethz.bootstrap.TextInput
import ch.ethz.todo.Client
import ch.ethz.todo.TaskList
import ch.ethz.todo.domain._
import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.raw.Event

object TodoList {

  val labelBus: EventBus[String] = new EventBus
  val labelSignal: Signal[String] = labelBus.events.startWith("")

  val newTaskBus: EventBus[Event] = new EventBus
  val newTaskStream: EventStream[TaskList] =
    newTaskBus.events
      .sample(labelSignal)
      .filter(_.trim.nonEmpty)
      .flatMap(label => Client.insertTask(Task(label, None, false)))
      .flatMap(_ => Client.tasks)

  val updateTaskBus: EventBus[(Int, Task)] = new EventBus
  val updateTaskStream: EventStream[TaskList] =
    updateTaskBus.events
      .flatMap((Client.updateTask _).tupled)
      .flatMap(_ => Client.tasks)

  val deleteTaskBus: EventBus[Int] = new EventBus
  val deleteTaskStream: EventStream[TaskList] =
    deleteTaskBus.events
      .flatMap(Client.deleteTask)
      .flatMap(_ => Client.tasks)

  def apply(tasksVar: Var[TaskList]) = {

    val taskList = tasksVar.signal.map(_.getOrElse(Nil))

    def tasksView(completed: Boolean) =
      ListGroup(
        children <-- taskList.map(
          _
            .filter(_._2.completed === completed)
            .map { case (id, task) =>
              ListGroupItem(
                cls := "d-flex align-items-center",
                Checkbox(cls := "flex-grow-1")(
                  checked := task.completed,
                  onChange.mapTo((id, task.copy(completed = !task.completed))) --> updateTaskBus.writer
                )(task.label),
                /*
                input(
                  cls := "form-control",
                  tpe := "datetime-local"
                ),
                //button(cls := "btn bi-calendar"),
                */
                button(
                  tpe := "button",
                  cls := "btn-close",
                  onClick.mapTo(id) --> deleteTaskBus.writer
                )
              )
            }
        )
      )

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
              value <-- newTaskStream.mapTo(""),
              onInput.mapToValue --> labelBus.writer,
              onEnterPress --> newTaskBus.writer
            ),
            button(
              cls := "btn btn-outline-secondary",
              `type` := "button",
              onClick --> newTaskBus.writer,
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
      ),
      newTaskStream --> tasksVar.writer,
      updateTaskStream --> tasksVar.writer,
      deleteTaskStream --> tasksVar.writer
    )
  }

  private val onEnterPress =
    onKeyPress.filter(_.keyCode == dom.ext.KeyCode.Enter)

}
