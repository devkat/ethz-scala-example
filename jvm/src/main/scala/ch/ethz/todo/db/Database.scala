package ch.ethz.todo.db

import cats.effect.Blocker
import cats.effect.IO
import cats.effect.Resource
import cats.implicits._
import ch.ethz.todo.domain.Task
import doobie.h2.H2Transactor
import doobie.implicits._
import doobie.implicits.javatime._
import doobie.util.transactor.Transactor
import doobie.util.update.Update

object Database {

  import doobie.util.ExecutionContexts

  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  val parentDir = "./data-dir"
  val dbName    = "todo-db"

  val transactor: Resource[IO, H2Transactor[IO]] =
    for {
      connectEC <- ExecutionContexts.fixedThreadPool[IO](32)
      blocker   <- Blocker[IO]
      xa <- H2Transactor.newH2Transactor[IO](
        url = "jdbc:h2:mem:todo;DB_CLOSE_DELAY=-1",
        user = "sa",
        pass = "",
        connectEC = connectEC, // await connection here
        blocker = blocker      // execute JDBC operations here
      )
    } yield xa

  val drop =
    sql"""
    DROP TABLE IF EXISTS task
  """.update.run

  val create =
    sql"""
    CREATE TABLE task (
      id SERIAL NOT NULL,
      label VARCHAR NOT NULL UNIQUE,
      date TIMESTAMP WITH TIME ZONE,
      completed BOOLEAN NOT NULL
    )
  """.update.run

  val initData = {
    val tasks = List(
      Task("Apples", None, false),
      Task("Milk", None, false),
      Task("Butter", None, true)
    )
    val sql = "insert into task(label, date, completed) values(?, ?, ?)"
    Update[Task](sql).updateMany(tasks)
  }

  def init(xa: Transactor[IO]) =
    Tuple3
      .apply(drop, create, initData)
      .mapN(_ + _ + _)
      .transact(xa)

}
