package ch.ethz.todo.view

import scalatags.Text.all.{title => _, _}
import scalatags.Text.tags2._

object Home {

  lazy val page = html(
    head(
      title("Tasks"),
      link(
        rel := "stylesheet",
        href := "https://cdn.jsdelivr.net/npm/bulma@0.9.2/css/bulma-rtl.min.css"
      ),
      script(
        src := "scripts/main.js"
      )
    ),
    body(
      section(
        `class` := "section",
        div(
          `class` := "container",
          h1(`class` := "title", "Tasks"),
          div(id := "app")
        )
      )
    )
  )

}
