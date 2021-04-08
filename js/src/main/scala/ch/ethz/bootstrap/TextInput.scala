package ch.ethz.bootstrap

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html.Input

object TextInput {

  def apply(modifiers: Modifier[ReactiveHtmlElement[Input]]*) = {
    input(
      className := "form-control",
      modifiers
    )
  }

}
