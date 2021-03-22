package ch.ethz.view

import scalatags.Text.all._
import scalatags.Text.tags2.title

object Home {

  def apply(name: String) = html(
    head(
      title("My Website")
    ),
    body(
      h1("My Website"),
      p(s"$name can count"),
      ul(
        (1 to 10).map(n => li(n.toString))
      )
    )
  )

}
