package ch.ethz.todo.components.bootstrap

import com.raquo.laminar.api.L._

object Checkbox {

  def apply(labelString: String) = {
    div(
      className := "form-check",
      input(
        className := "form-check-input",
        `type` := "checkbox"
      ),
      label(
        className := "form-check-label",
        labelString
      )
    )
  }

}
