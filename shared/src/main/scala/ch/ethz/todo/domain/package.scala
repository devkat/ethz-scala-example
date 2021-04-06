package ch.ethz.todo

import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.Decoder
import io.circe.Encoder

package object domain {

  final case class Task(label: String)
  
  object Task {
    implicit val decoder: Decoder[Task] = deriveDecoder
    implicit val encoder: Encoder[Task] = deriveEncoder
  }

}
