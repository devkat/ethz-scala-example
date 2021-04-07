package ch.ethz.bootstrap

import com.raquo.laminar.api.L._
import org.scalajs.dom.html.Input
import com.raquo.laminar.nodes.ReactiveHtmlElement

object TextInput {

  def apply(modifiers: Modifier[ReactiveHtmlElement[Input]]*) = {
    input(
      className := "form-control",
      modifiers
    )
  }

}
