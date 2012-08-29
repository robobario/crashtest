onSuccess = (data) =>
  render = (method) =>
    def =  method.methodDefinition
    methodDescription = def.name
    if def.parameters? and def.parameters.length > 0
      methodDescription += " (" + (param.name for param in def.parameters).join(",") + ")"
    $("#remoteMethods").append($('<div>').draggable({ revert: true , revertDuration: 200 }).data("method",def).addClass('well').append($('<span>').text(methodDescription)))
  render remoteMethod for remoteMethod in data

doCreate = () ->
  try
    create()
  catch error
    alertMessage(error.toString(), "alert-error")

alertMessage = (message, alertStyle) ->
  $("#messageContainer").append($('<div>').addClass('alert ' + alertStyle).alert().append($('<button>').attr({type:"button", class:"close", "data-dismiss":"alert"}).text('x')).append($('<strong>').text(message)))

create = () ->
  scriptName = $("#inputName").val()
  if !scriptName? or scriptName.length <= 0
    throw "script name must be provided"
  getParameters = (element) ->
    toMessage = (index, element) ->
      value =  $(element).val()
      type =  $(element).attr('expression-type')
      if !value? or value.length <= 0
        throw "all expressions must be completed"
      if !type? or type.length <= 0
        throw "expression type must be provided"
      message =
        value : value
        "@type" : type
    $(element).children("input").map(toMessage).toArray()
  toMessage = (index, element) ->
    methodData = $(element).data("method")
    name = methodData.name
    if !name? or name.length <= 0
      throw "method name must be provided"
    message =
      name : methodData.name
      "@type" : "remote invocation"
      parameterExpressions : getParameters(element)
  message  = JSON.stringify({name:scriptName, statementRequests : $("div.statement").map(toMessage).toArray()})
  sendCreateMessage(message)

sendCreateMessage = (data) ->
  message =
    type : "POST",
    url : "/createScript",
    data : data,
    contentType : "text/json",
    dataType : 'json'
    success : onCreationSuccess
  $.ajax(message)

onCreationSuccess = (data) ->
  if data.errors.length == 0
    alertMessage("success","alert-success")
  else
    alertMessage(data.errors[0].toString(),"alert-error")

$(()->
  onDrop = (event, ui) ->
    method = ui.draggable.data("method")
    div = $("<div>").data("method",method).addClass("statement")
    div.append("<span>").text(method.name + " ")
    appendInput = (element, paramName) ->
      element.append($("<input>").addClass('input-small').attr({"placeholder" : paramName, "type" : "text", "expression-type" : "literal"}))
    appendInput(div, param.name) for param in method.parameters
    $("#statements").append(div)

  $("#create").click(doCreate)

  $("#script-dropzone").droppable({drop : onDrop})
  message =
    type : "GET",
    url : "/remoteMethods",
    contentType : "text/json",
    dataType : 'json'
    success : onSuccess
  $.ajax(message)
)
