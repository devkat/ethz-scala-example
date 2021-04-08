package ch.ethz.bootstrap

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html.LI

object ListGroupItem {

  def apply(modifiers: Modifier[ReactiveHtmlElement[LI]]*) = {
    li(
      className := "list-group-item",
      modifiers
    )
  }

}
