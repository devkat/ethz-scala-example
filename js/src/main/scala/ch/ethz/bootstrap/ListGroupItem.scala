package ch.ethz.todo.components.bootstrap

import com.raquo.laminar.api.L._
import org.scalajs.dom.html.LI
import com.raquo.laminar.nodes.ReactiveHtmlElement

object ListGroupItem {

  def apply(modifiers: Modifier[ReactiveHtmlElement[LI]]*) = {
    li(
      className := "list-group-item",
      modifiers
    )
  }

}
