package ch.ethz

import scala.util.Try

object StateMonadExample extends App {

  final case class State[S, A](run: S => (S, A)) {

    def map[B](f: A => B): State[S, B] = State(s => {
      val (t, a) = run(s)
      (t, f(a))
    })

    def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
      val (t, a) = run(s)
      f(a).run(t)
    })

  }


  object State {

    def get[S]: State[S, S] = State(s => (s, s))

    def set[S](s: S): State[S, Unit] = State(_ => (s, ()))

    def modify[S](f: S => S): State[S, Unit] = State(s => (f(s), ()))

    def value[S, A](a: A): State[S, A] = State(s => (s, a))

  }

  import State._

  def extractNumbers: State[List[String], List[Int]] =
    for {
      list <- get[List[String]]
      numbers <- list match {
        case Nil => value[List[String], List[Int]](Nil)
        case h :: t =>
          for {
            _ <- set(t)
            numbers2 <- Try(Integer.parseInt(h)).fold(
              _ => extractNumbers,
              n => extractNumbers.map(l => l :+ n)
            )
          } yield numbers2
      }
    } yield numbers

  println(extractNumbers.run(List("1", "foo", "bar", "23", "baz"))._2)

  type T = List[Int]

  val result = for {
    _ <- set(List(1, 2, 3))
    list <- get[T]
    _ = println(list)
    _ <- modify[T](_ :+ 4)
  } yield ()

  println(result.run(List.empty))

}
