package ch.ethz.todo.view

import scalatags.Text.all.{title => _, _}
import scalatags.Text.tags2._

object Home {

  private def stylesheet(url: String) = link(rel := "stylesheet", href := url)

  lazy val page = html(
    head(
      title("Tasks"),
      stylesheet("https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css"),
      stylesheet( "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"),
      script(src := "scripts/main.js")
    ),
    body(
      div(
        `class` := "container-sm my-5",
        div(id := "app")
      )
    )
  )

}
