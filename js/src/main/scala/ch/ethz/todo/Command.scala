package ch.ethz.todo

sealed trait Command

object Command {
  case class Create(label: String) extends Command
  case class UpdateCompleted(id: Int, completed: Boolean) extends Command
}
