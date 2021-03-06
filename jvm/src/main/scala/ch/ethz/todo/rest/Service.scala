package ch.ethz.todo.rest

import cats.effect.IO
import ch.ethz.todo.domain._
import doobie.implicits._
import doobie.implicits.javatime._
import doobie.util.transactor
import doobie.util.update.Update
import java.time.Instant

trait Service[F[_]] {

  def tasks: F[List[(Int, Task)]]

  def insertTask(task: Task): F[Task]

  def updateTask(id: Int, task: Task): F[Task]

  def deleteTask(id: Int): F[Unit]

}

object Service {

  def apply[F[_]](implicit instance: Service[F]): Service[F] = instance

  def ioInstance(xa: transactor.Transactor[IO]): Service[IO] =
    new Service[IO] {

      override def tasks: IO[List[(Int, Task)]] =
        sql"select id, label, date, completed from task order by id desc"
          .query[(Int, String, Option[Instant], Boolean)]
          .map { case (id, label, date, completed) => (id, Task(label, date, completed)) }
          .to[List]
          .transact(xa)

      override def insertTask(task: Task): IO[Task] =
        Update[Task]("insert into task(label, date, completed) values (?, ?, ?)")
          .updateMany(List(task))
          .transact(xa)
          .as(task)

      override def updateTask(id: Int, task: Task): IO[Task] =
        sql"update task set label = ${task.label}, completed = ${task.completed} where id = $id"
          .update
          .run
          .transact(xa)
          .as(task)

      override def deleteTask(id: Int): IO[Unit] =
      sql"delete from task where id = $id"
          .update
          .run
          .transact(xa)
          .void

    }

}
