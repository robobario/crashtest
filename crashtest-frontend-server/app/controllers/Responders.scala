package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.ws.WS
import play.api.libs.json.{Json, JsValue}

object Responders extends Controller{

  def createRemoteMethodView() = Action{
    Ok(views.html.createRemoteMethod.render())
  }

  def createRemoteMethod() = Action(parse.json){ request=>
    val body: JsValue = request.body
    val name: String = (body \ "name").as[String]
    val toParam: (String) => JsValue = i => Json.toJson(Map("name" -> Json.toJson(i)))
    val paramNames: Seq[JsValue] = ((body \ "params") \\ "name").map(_.as[String]).map(toParam)
    val data: JsValue = Json.toJson(Map("name" -> Json.toJson(name), "parameters" -> Json.toJson(paramNames)))
    Async {
      val stringify: String = Json.stringify(data)
      println("woot")
      println(stringify)
      WS.url("http://localhost:8180/crashtest/newRemoteMethod").withHeaders("Content-Type"->"application/json","Accept"->"application/json").post(stringify).map { response =>
        Ok(Json.parse(response.body))
      }
    }
  }
}
