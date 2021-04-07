package ch.ethz.todo.rest

import ch.ethz.todo.domain._
import doobie.implicits._
import doobie.util.transactor
import cats.effect.IO
import doobie.util.update.Update

trait Service[F[_]] {

  def tasks: F[List[(Int, Task)]]

  def insertTask(task: Task): F[Task]

  def updateTask(id: Int, task: Task): F[Task]

}

object Service {

  def apply[F[_]](implicit instance: Service[F]): Service[F] = instance

  def ioInstance(xa: transactor.Transactor[IO]): Service[IO] =
    new Service[IO] {

      @Override
      def tasks: IO[List[(Int, Task)]] =
        sql"select id, label, completed from task order by id desc"
          .query[(Int, String, Boolean)]
          .map { case (id, label, completed) => (id, Task(label, completed)) }
          .to[List]
          .transact(xa)

      @Override
      def insertTask(task: Task): IO[Task] =
        Update[Task]("insert into task(label, completed) values (?, ?)")
          .updateMany(List(task))
          .transact(xa)
          .as(task)

      @Override
      def updateTask(id: Int, task: Task): IO[Task] =
        sql"udpate task set label = ${task.label}, completed = ${task.completed} where id = $id"
          .update
          .transact(xa)
          .as(task)

    }

}
