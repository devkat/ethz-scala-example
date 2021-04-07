package ch.ethz.todo

package object domain {

  final case class Task(
      label: String,
      completed: Boolean
  )

}
