package controllers

import play.api.mvc.{Action, Controller}

object Responders extends Controller{

  def createRemoteMethod() = Action{
    Ok(views.html.createRemoteMethod.render())
  }
}
