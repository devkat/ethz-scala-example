package ch.ethz

import ch.ethz.todo.domain.Task

package object todo {

  type ErrorMessage = String

  type TaskList = Either[ErrorMessage, List[(Int, Task)]]

}
