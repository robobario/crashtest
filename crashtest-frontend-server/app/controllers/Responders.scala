package controllers

import play.api.mvc.{Action, Controller}

object Responders extends Controller{

  def index() = Action{
    Ok(views.html.index.render())
  }
}
