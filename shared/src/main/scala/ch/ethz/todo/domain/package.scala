package ch.ethz.todo

import java.time.Instant

package object domain {

  final case class Task(
      label: String,
      date: Option[Instant],
      completed: Boolean
  )

}
