package ch.ethz

import scala.collection.mutable
import scala.collection.parallel.CollectionConverters._
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

object Example extends App {


  def f() = {

  def factorial(n: Int): Int = 
    if (n > 1) n * factorial(n - 1) else n

  def timed[A](f: => A): Duration = {
    val parStart = System.currentTimeMillis()
    f
    val parEnd = System.currentTimeMillis()
    Duration(parEnd - parStart, TimeUnit.MILLISECONDS)
  }

  val mutableNumbers = mutable.Seq.range(10000, 50001)
  def loop =
    for { i <- (0 until mutableNumbers.size) }
      mutableNumbers(i) = factorial(mutableNumbers(i))


  val numbers = 10000 until 50000

  val loopTime = timed(loop)
  val seqTime = timed(numbers.map(factorial))
  val parTime = timed(numbers.par.map(factorial))

  println("Loop:       " + loopTime)
  println("Sequential: " + seqTime)
  println("Parallel:   " + parTime)
  println("Factor:     " + seqTime / parTime)
  }

  def factorial2(n: Int): Int = {
    var f = 1
    for (i <- 1 to n) {
      f = f * i;
    }
    f
  }

  def factorial(n: Int): Int =
    if (n == 0) 1 else n * factorial(n - 1)

  println(factorial(5))

}
