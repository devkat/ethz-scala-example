package ch.ethz.bootstrap

import com.raquo.laminar.api.L._
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html.UList

object ListGroup {

  def apply(modifiers: Modifier[ReactiveHtmlElement[UList]]*) = {
    ul(
      className := "list-group",
      modifiers
    )
  }

}
