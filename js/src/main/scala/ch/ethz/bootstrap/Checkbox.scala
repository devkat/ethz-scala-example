package ch.ethz.bootstrap

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html.{Input, Label, Div}

object Checkbox {

  def apply(
      groupModifiers: Modifier[ReactiveHtmlElement[Div]]*
  )(
      inputModifiers: Modifier[ReactiveHtmlElement[Input]]*
  )(
      labelModifiers: Modifier[ReactiveHtmlElement[Label]]*
  ) = {
    div(
      className := "form-check",
      groupModifiers,
      input(
        className := "form-check-input",
        `type` := "checkbox",
        inputModifiers
      ),
      label(
        className := "form-check-label",
        labelModifiers
      )
    )
  }

}
