package ch.ethz.rest

object Service {

  case class User(id: Int, name: String)

  def user(id: Int) =
    User(id, "Sam Vimes")

}
