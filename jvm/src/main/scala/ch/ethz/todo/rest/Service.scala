package ch.ethz.todo.rest

import ch.ethz.todo.domain._

object Service {

  def tasks =
    List(
      Task("Apples", false),
      Task("Milk", false),
      Task("Butter", true)
    )

}
