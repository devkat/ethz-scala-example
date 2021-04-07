package ch.ethz.todo

//import ch.ethz.todo.Command
//import ch.ethz.todo.Command._
import ch.ethz.todo.components.TodoList
import ch.ethz.todo.domain.Task
import com.raquo.laminar.api.L._

import org.scalajs.dom

object Main {

  private val tasksVar: Var[Either[String, List[(Int, Task)]]] = Var(Right(Nil))

  /*
  private val commandObserver = Observer[Command] {
    case Create(label) =>
      //Client.insertTask(Task(label, false))
      //tasksVar.update(_ :+ Task(id = lastId, text = itemText, completed = false))
    case UpdateCompleted(id, completed) =>
      //Client.updateTask(id, Task("foo", completed))
      tasksVar.update(_.map(item =>
        if (item.id == itemId) item.copy(completed = completed) else item
      ))
  }
*/
  private def init = {
    val appContainer: dom.Element = dom.document.getElementById("app")
    val appElement = div(
      className := "content",
      child <-- tasksVar.signal.map(_.fold(div("Error: ", _), _ => div())),
      TodoList(tasksVar)
      //TodoList(/*commandObserver, */Client.tasks.map(_.getOrElse(Seq.empty)))
    )
    Client.tasks.foreach(tasksVar.set)(unsafeWindowOwner)
    render(appContainer, appElement)
  }

  def main(args: Array[String]): Unit = {
    documentEvents.onDomContentLoaded.foreach(_ => init)(unsafeWindowOwner)
  }

}
