class Parameter
  constructor: (@name) ->

  getName: () ->
    @name

class RemoteMethod
  constructor: () ->
    @parameters = []

  addParameter: (parameter) ->
    if $.inArray(parameter, @parameters) == -1
      @parameters.push(parameter)

  removeParameter: (parameter) ->
    @parameters = @parameters.filter (param) -> param isnt parameter

  getParameters: () ->
    @parameters

model = new RemoteMethod()

class View
  constructor: (@model) ->

  update : () ->
    ul = $("#parameters")
    ul.empty()
    params = model.getParameters()
    update = (param) ->
      ul.append($('<li>').text(param).append($('<a>').click(()->controller.removeParameter(param)).attr('href','#').addClass('icon-minus-sign')))
    update param for param in params

class Controller
  constructor: (@model,@view) ->

  addParameter : (parameter) ->
    @model.addParameter(parameter)
    @view.update()

  removeParameter : (parameter) ->
    @model.removeParameter(parameter)
    @view.update()

view = new View model
controller = new Controller model, view

onAddParamClick = () ->
  paramName = $("#inputParam").val()
  if paramName? and paramName.length > 0
    controller.addParameter(paramName)

error = (message) ->
  alertMessage(message  ,"alert-error")

onSuccess = (data) ->
  if data.errors.length == 0
    alertMessage("success","alert-success")
  else
    alertMessage(data.errors[0],"alert-error")

alertMessage = (message, alertStyle) ->
  $("#messageContainer").append($('<div>').addClass('alert ' + alertStyle).alert().append($('<button>').attr({type:"button", class:"close", "data-dismiss":"alert"}).text('x')).append($('<strong>').text(message)))

onCreate = () ->
  methodName = $("#inputName").val()
  if !methodName? or methodName.length == 0
    error("Could not create method with no name")
  else
    data =
      name : methodName
      params : model.getParameters()
    message =
      type : "POST",
      url : "/createRemoteMethod",
      contentType : "text/json",
      dataType : 'json',
      data : JSON.stringify(data),
      success : onSuccess
    $.ajax(message)

$(()->
  $("#addParamButton").click(onAddParamClick)
  $("#create").click(onCreate)
)