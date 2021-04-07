package ch.ethz.todo.view

import scalatags.Text.all.{title => _, _}
import scalatags.Text.tags2._

object Home {

  lazy val page = html(
    head(
      title("Tasks"),
      link(
        rel := "stylesheet",
        href := "https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css"
      ),
      script(
        src := "scripts/main.js"
      )
    ),
    body(
      div(
        `class` := "container-sm my-5",
        div(id := "app")
      )
    )
  )

}
