package controllers

import play.api.mvc.{SimpleResult, Result, Action, Controller}
import play.api.libs.ws.{Response, WS}
import play.api.libs.json.{Json, JsValue}
import play.api.libs.concurrent.Promise

object Responders extends Controller{

  def createRemoteMethodView() = Action{
    Ok(views.html.createRemoteMethod.render())
  }

  def createRemoteMethod() = Action(parse.json){ request=>
    val body: JsValue = request.body
    val name: String = (body \ "name").as[String]
    val toParam: (String) => JsValue = i => Json.toJson(Map("name" -> Json.toJson(i)))
    val paramNames: Seq[JsValue] = (body \ "params").as[Seq[JsValue]].map(_.as[String]).map(toParam)
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

  def remoteMethodOverview() = Action{
    Ok(views.html.remoteMethodOverview.render())
  }

  def retrieve(response: Response): Promise[Result] = {
    val details: Seq[String] = (Json.parse(response.body) \ "remoteMethodDetails").as[Seq[JsValue]].map(_.as[String])
    val responses: Seq[Promise[JsValue]] = details.map(detail => WS.url("http://localhost:8180/" + detail).withHeaders("Accept" -> "application/json").get().map(response => Json.parse(response.body)))
    val sequence: Promise[Seq[JsValue]] = Promise.sequence(responses)
    sequence.map(details => Ok(Json.toJson(details)))
  }

  def getScripts(response: Response): Promise[Result] = {
    val details: Seq[String] = (Json.parse(response.body) \ "allScriptDetailsUrls").as[Seq[JsValue]].map(_.as[String])
    val responses: Seq[Promise[JsValue]] = details.map(detail => WS.url("http://localhost:8180/" + detail).withHeaders("Accept" -> "application/json").get().map(response => Json.parse(response.body)))
    val sequence: Promise[Seq[JsValue]] = Promise.sequence(responses)
    sequence.map(details => createResponseForAllScriptDetails(details))
  }


  def createResponseForAllScriptDetails(details: scala.Seq[JsValue]): SimpleResult[JsValue] = {
    val errors: Seq[JsValue] = details.flatMap(value => (value \ "errors").as[Seq[JsValue]])
    val scripts: Seq[JsValue] = details.map(value => (value \ "script"))
    Ok(Json.toJson(Map("errors" -> errors, "scripts" -> scripts)))
  }

  def remoteMethodList() = Action{
    Async {
      WS.url("http://localhost:8180/crashtest/remote-methods").withHeaders("Accept"->"application/json").get().flatMap { response =>
        retrieve(response)
      }
    }
  }

  def createScriptView() = Action{
    Ok(views.html.createScript.render())
  }

  def createScript() = Action(parse.json){ request =>
    Async {
      WS.url("http://localhost:8180/crashtest/newScript").withHeaders("Content-Type"->"application/json","Accept"->"application/json").post(body = request.body).map { response =>
        Ok(Json.toJson(Map("errors" -> ((Json.parse(response.body))\ "errors").as[Seq[JsValue]])))
      }
    }
  }

  def createMethod() = Action(parse.json){ request =>
    Async {
      WS.url("http://localhost:8180/crashtest/newMethod").withHeaders("Content-Type"->"application/json","Accept"->"application/json").post(body = request.body).map { response =>
        Ok(Json.toJson(Map("errors" -> ((Json.parse(response.body))\ "errors").as[Seq[JsValue]])))
      }
    }
  }

  def displayAllScripts() = Action{
    Ok(views.html.scriptOverview.render())
  }


  def getAllScripts = Action{ request =>
    Async {
      WS.url("http://localhost:8180/crashtest/scripts").withHeaders("Accept"->"application/json").get().flatMap { response =>
        getScripts(response)
      }
    }
  }

  def getAllMethods = Action{ request =>
    Async {
      WS.url("http://localhost:8180/crashtest/methods").withHeaders("Accept"->"application/json").get().flatMap { response =>
        getMethods(response)
      }
    }
  }

  def getMethods(response: Response): Promise[Result] = {
    val details: Seq[String] = (Json.parse(response.body) \ "methodDetails").as[Seq[JsValue]].map(_.as[String])
    val responses: Seq[Promise[JsValue]] = details.map(detail => WS.url("http://localhost:8180/" + detail).withHeaders("Accept" -> "application/json").get().map(response => Json.parse(response.body)))
    val sequence: Promise[Seq[JsValue]] = Promise.sequence(responses)
    sequence.map(details => createResponseForAllMethodDetails(details))
  }

  def createResponseForAllMethodDetails(details: scala.Seq[JsValue]): SimpleResult[JsValue] = {
    val errors: Seq[JsValue] = details.flatMap(value => (value \ "errors").as[Seq[JsValue]])
    val methods: Seq[JsValue] = details.map(value => (value \ "methodDefinition"))
    Ok(Json.toJson(Map("errors" -> errors, "methodDefinitions" -> methods)))
  }


  def overview = Action{
    Ok(views.html.overview.render())
  }
}
