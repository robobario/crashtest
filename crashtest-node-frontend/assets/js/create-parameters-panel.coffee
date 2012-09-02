window.CreateParameterPanel = class CreateParameterPanel
  constructor: (element) ->
    @model = new Parameters()
    @view = new ParametersView @model, element
    @controller = new ParametersController @model, @view
    @view.setController(@controller)

  getParameters: () ->
    @model.getParameters()

  render: () ->
    @view.update()

class Parameters
  constructor: () ->
    @parameters = []

  addParameter: (parameter) ->
    if parameter.length > 0 and $.inArray(parameter, @parameters) == -1
      @parameters.push(parameter)

  removeParameter: (parameter) ->
    @parameters = @parameters.filter (param) -> param isnt parameter
    $(document).trigger("parameter-removed",parameter)

  getParameters: () ->
    @parameters

class ParametersView
  constructor: (@model, @container) ->

  setController : (controller) ->
    @controller = controller

  update : () ->
    @container.empty()
    params = @model.getParameters()
    update = (param) =>
      inner = $('<div>').addClass("control-group").appendTo(@container).draggable({revert:true,revertDuration:0}).data("param",param)
      append = $('<div>').addClass("input-append").appendTo(inner)
      control = $('<div>').addClass("controls").appendTo(append)
      dragger = $('<span>').addClass("icon-move").appendTo(control)
      input = $("<input disabled type=\"text\">").val(param).appendTo(control)
      button = $('<a>').click(()=>@controller.removeParameter(param)).attr('href','#').addClass("btn btn-danger").appendTo(control)
      icon = $('<i>').addClass("icon-minus-sign").addClass("icon-white").appendTo(button)
    update param for param in params
    paramComponent = $("<div>").addClass("control-group").appendTo(@container)
    paramInputDiv = $("<div>").addClass("controls").appendTo(paramComponent)
    innerParamDiv = $("<div>").addClass("input-append").appendTo(paramInputDiv)
    paramButton = $("<input>").attr({id:"paramNameInput",type:"text"})
    paramButton.after($("<a>").click(()=>@controller.addParameter($("#paramNameInput").val())).attr({class:"btn btn-success",href:"#"})).appendTo(innerParamDiv)
    icon = $('<i>').addClass("icon-plus-sign").addClass("icon-white").appendTo(paramButton)
    paramButton.append($('<span>').text(" Add Parameter"))

class ParametersController
  constructor: (@model,@view) ->

  addParameter : (parameter) ->
    @model.addParameter(parameter)
    @view.update()

  removeParameter : (parameter) ->
    @model.removeParameter(parameter)
    @view.update()

new window.CreateParameterPanel $('body')