class Parameters
  constructor: () ->
    @parameters = []

  addParameter: (parameter) ->
    if parameter.length > 0 and $.inArray(parameter, @parameters) == -1
      @parameters.push(parameter)
      if @add?
        @add(parameter)

  removeParameter: (parameter) ->
    @parameters = @parameters.filter (param) -> param isnt parameter
    if @remove?
      @remove(parameter)

  getParameters: () ->
    @parameters

  setOnParameterAdd : (func) ->
    @add = func

  setOnParameterRemove : (func) ->
    @remove = func

class ParametersView
  constructor: (@model) ->

  setController : (controller) ->
    @controller = controller

  update : () ->
    div = $("#new-param-container")
    div.empty()
    params = @model.getParameters()
    controller = @controller
    update = (param) ->
      inner = $('<div>').addClass("control-group").appendTo(div).draggable({revert:true,revertDuration:0}).data("param",param)
      append = $('<div>').addClass("input-append").appendTo(inner)
      control = $('<div>').addClass("controls").appendTo(append)
      dragger = $('<span>').addClass("icon-move").appendTo(control)
      input = $("<input disabled type=\"text\">").val(param).appendTo(control)
      button = $('<a>').click(()->controller.removeParameter(param)).attr('href','#').addClass("btn btn-danger").appendTo(control)
      icon = $('<i>').addClass("icon-minus-sign").addClass("icon-white").appendTo(button)
    update param for param in params

class ParametersController
  constructor: (@model,@view) ->

  addParameter : (parameter) ->
    @model.addParameter(parameter)
    @view.update()

  removeParameter : (parameter) ->
    @model.removeParameter(parameter)
    @view.update()


class DetailsElementModel
  constructor: () ->
    @mode = "welcome"

  getMode: () ->
    @mode

  setMode: (mode) ->
    @mode = mode

class DetailsElementView
  constructor: (@model) ->

  redraw: () ->
    $(".remoteMethodDragger").addClass("hidden")
    mode = @model.getMode()
    switch mode
      when "welcome" then this.renderWelcome()
      when "createRemote" then this.renderCreateRemote()
      when "createScript" then this.renderCreateScript()
      when "createMethod" then this.renderCreateMethod()

  renderCreateMethod: () ->
    model = new Parameters()
    view = new ParametersView model
    controller = new ParametersController model, view
    view.setController(controller)

    onDrop = (event, ui) ->
      method = ui.draggable.data("method")
      if method?
        onParamDrop = (event, ui) ->
          param = ui.draggable.data("param")
          if param?
            input = $(this)
            onDelete = () ->
              input.removeAttr("disabled").val("").attr({"placeholder" : paramName, "type" : "text", "expression-type" : "literal"})
              $(this).remove()
            input.attr('disabled':'disabled',"expression-type" : "identifier").val(param).after($("<a>").attr("href","#").addClass("icon-minus-sign").click(onDelete))
        tr = $("<tr>").data("method",method).addClass("statement")
        td = $("<td>").text(method.name + " ").appendTo(tr)
        appendInput = (element, paramName) ->
          container = $("<div>").addClass("input-append dropdown").appendTo(element)
          button = $("<input>").droppable({drop : onParamDrop, tolerance:"pointer"}).addClass('input-small').attr({"placeholder" : paramName, "type" : "text", "expression-type" : "literal"})
          container.append(button)
        appendInput(td, param.name) for param in method.parameters
        $("#methodStatementTable").append(tr)

    create = () ->
      methodName = $("#methodNameInput").val()
      if !methodName? or methodName.length <= 0
        throw "method name must be provided"
      getParameters = (element) ->
        toMessage = (index, element) ->
          value =  $(element).val()
          type =  $(element).attr('expression-type')
          if !value? or value.length <= 0
            throw "all expressions must be completed"
          if !type? or type.length <= 0
            throw "expression type must be provided"
          message =
            "@type" : type
          if type == "literal"
            message["value"] = value
          else
            message["name"] = value
          message
        $(element).find("input").map(toMessage).toArray()
      toMessage = (index, element) ->
        methodData = $(element).data("method")
        name = methodData.name
        if !name? or name.length <= 0
          throw "method name must be provided"
        parameterExpressions = getParameters(element)
        message =
          name : methodData.name
          "@type" : methodData.statementType
          parameterExpressions : parameterExpressions
        message
      parameters = ({"name" : param} for param in model.getParameters())
      message  = JSON.stringify({name:methodName,parameters:parameters, statements : $("#methodStatementTable > tr").map(toMessage).toArray()})
      sendCreateMethodMessage(message)
    doCreate = () ->
      try
        create()
      catch error
        alertMessage(error.toString(), "alert-error")

    outerDiv = $("<div>").addClass("well")
    form = $("<form>").addClass("form-horizontal").appendTo(outerDiv)
    legend = $("<legend>").text("Create A Method").appendTo(form)
    nameComponent = $("<div>").addClass("control-group").appendTo(form)
    nameLabel = $("<label>").attr({class:"control-label" ,for:"methodNameInput"}).text("Name").appendTo(nameComponent)
    nameInputDiv = $("<div>").addClass("controls").appendTo(nameComponent)
    nameInput = $("<input>").attr({placeholder:"Method Name", id:"methodNameInput", type:"text"}).appendTo(nameInputDiv)

    newParamContainer = $("<div>").attr({id:"new-param-container"}).appendTo(form)

    paramComponent = $("<div>").addClass("control-group").appendTo(form)
    paramInputDiv = $("<div>").addClass("controls").appendTo(paramComponent)
    innerParamDiv = $("<div>").addClass("input-append").appendTo(paramInputDiv)
    paramButton = $("<input>").attr({id:"paramNameInput",type:"text"}).after($("<a>").click(()->controller.addParameter($("#paramNameInput").val())).addClass("btn btn-success").attr({href:"#"})).appendTo(innerParamDiv)
    icon = $('<i>').addClass("icon-plus-sign").addClass("icon-white").appendTo(paramButton)
    paramButton.append($('<span>').text(" Add Parameter"))

    legend = $("<legend>").text("Statements").appendTo(form)
    dropzone = $("<div>").droppable({drop : onDrop, tolerance : "pointer"}).addClass("dropZone").text("drag and drop methods and remote methods on me").appendTo(form)
    droptable = $("<table>").addClass("table table-striped").appendTo(form)
    dropbody = $("<tbody>").attr({id:"methodStatementTable"}).appendTo(droptable)

    createComponent = $("<div>").addClass("control-group").appendTo(form)
    createInputDiv = $("<div>").addClass("controls").appendTo(createComponent)
    createButton = $("<a>").click(doCreate).addClass("btn btn-primary").attr({href:"#"}).text("Create").appendTo(createInputDiv)
    $('#detailsArea').empty().append($(outerDiv))
    $(".remoteMethodDragger").removeClass("hidden")


  renderCreateScript: () ->
    onDrop = (event, ui) ->
      method = ui.draggable.data("method")
      tr = $("<tr>").data("method",method).addClass("statement")
      td = $("<td>").text(method.name + " ").appendTo(tr)
      appendInput = (element, paramName) ->
        element.append($("<input>").addClass('input-small').attr({"placeholder" : paramName, "type" : "text", "expression-type" : "literal"}))
      appendInput(td, param.name) for param in method.parameters
      $("#scriptStatementTable").append(tr)

    create = () ->
      scriptName = $("#scriptNameInput").val()
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
        $(element).find("input").map(toMessage).toArray()
      toMessage = (index, element) ->
        methodData = $(element).data("method")
        name = methodData.name
        if !name? or name.length <= 0
          throw "method name must be provided"
        parameterExpressions = getParameters(element)
        message =
          name : methodData.name
          "@type" : methodData.statementType
          parameterExpressions : parameterExpressions
        message
      message  = JSON.stringify({name:scriptName, statementRequests : $("#scriptStatementTable > tr").map(toMessage).toArray()})
      sendCreateScriptMessage(message)
    doCreate = () ->
      try
        create()
      catch error
        alertMessage(error.toString(), "alert-error")

    outerDiv = $("<div>").addClass("well")
    form = $("<form>").addClass("form-horizontal").appendTo(outerDiv)
    legend = $("<legend>").text("Create A Test Script").appendTo(form)
    nameComponent = $("<div>").addClass("control-group").appendTo(form)
    nameLabel = $("<label>").attr({class:"control-label" ,for:"scriptNameInput"}).text("Name").appendTo(nameComponent)
    nameInputDiv = $("<div>").addClass("controls").appendTo(nameComponent)
    nameInput = $("<input>").attr({placeholder:"Script Name", id:"scriptNameInput", type:"text"}).appendTo(nameInputDiv)
    legend = $("<legend>").text("Statements").appendTo(form)
    dropzone = $("<div>").droppable({drop : onDrop, tolerance : "pointer"}).addClass("dropZone").text("drag and drop methods and remote methods on me").appendTo(form)
    droptable = $("<table>").addClass("table table-striped").appendTo(form)
    dropbody = $("<tbody>").attr({id:"scriptStatementTable"}).appendTo(droptable)

    createComponent = $("<div>").addClass("control-group").appendTo(form)
    createInputDiv = $("<div>").addClass("controls").appendTo(createComponent)
    createButton = $("<a>").click(doCreate).addClass("btn btn-primary").attr({href:"#"}).text("Create").appendTo(createInputDiv)
    $('#detailsArea').empty().append($(outerDiv))
    $(".remoteMethodDragger").removeClass("hidden")

  renderCreateRemote: () ->
    model = new Parameters()
    view = new ParametersView model
    controller = new ParametersController model, view
    view.setController(controller)

    outerDiv = $("<div>").addClass("well")
    form = $("<form>").addClass("form-horizontal").appendTo(outerDiv)
    legend = $("<legend>").text("Create A Remote Method").appendTo(form)
    nameComponent = $("<div>").addClass("control-group").appendTo(form)
    nameLabel = $("<label>").attr({class:"control-label" ,for:"inputName"}).text("Name").appendTo(nameComponent)
    nameInputDiv = $("<div>").addClass("controls").appendTo(nameComponent)
    nameInput = $("<input>").attr({placeholder:"Remote Method Name", id:"inputName", type:"text"}).appendTo(nameInputDiv)

    newParamContainer = $("<div>").attr({id:"new-param-container"}).appendTo(form)

    paramComponent = $("<div>").addClass("control-group").appendTo(form)
    paramInputDiv = $("<div>").addClass("controls").appendTo(paramComponent)
    innerParamDiv = $("<div>").addClass("input-append").appendTo(paramInputDiv)
    paramButton = $("<input>").attr({id:"paramNameInput",type:"text"}).after($("<a>").click(()->controller.addParameter($("#paramNameInput").val())).addClass("btn btn-success").attr({href:"#"})).appendTo(innerParamDiv)
    icon = $('<i>').addClass("icon-plus-sign").addClass("icon-white").appendTo(paramButton)
    paramButton.append($('<span>').text(" Add Parameter"))

    createComponent = $("<div>").addClass("control-group").appendTo(form)
    createInputDiv = $("<div>").addClass("controls").appendTo(createComponent)
    createButton = $("<a>").addClass("btn btn-primary").attr({href:"#"}).text("Create").appendTo(createInputDiv)
    $('#detailsArea').empty().append($(outerDiv))
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
          success : onSuccessfulCreation
        $.ajax(message)
    createButton.click(onCreate)

  renderWelcome: () ->
    $('#detailsArea').empty().append($("<div>").addClass("hero-unit").append($('<h1>').text("Get Testing")).append($('<p>').text("Define remote methods, call them from methods, arrange them into scripts")))



class DetailsElementController
  constructor: (@model, @view) ->

  beginCreatingRemoteMethod: () ->
    @model.setMode("createRemote")
    @view.redraw()

  beginCreatingScript: () ->
    @model.setMode("createScript")
    @view.redraw()

  beginCreatingMethod: () ->
    @model.setMode("createMethod")
    @view.redraw()

  showWelcome: () ->
    @model.setMode("welcome")
    @view.redraw()

class TestElementModel
  constructor: () ->
    @remoteMethods = {}
    @methods = {}
    @scripts = {}

  addScript: (script) ->
    @scripts[script.name] = script

  getAllScripts: () ->
    (value for prop, value of  @scripts)

  addRemoteMethod: (remoteMethod) ->
    remoteMethod.statementType = "remote invocation"
    @remoteMethods[remoteMethod.name] = remoteMethod

  getAllRemoteMethods: () ->
    (value for prop, value of  @remoteMethods)

  addMethod: (method) ->
    method.statementType = "method invocation"
    @methods[method.name] = method

  getAllMethods: () ->
    (value for prop, value of  @methods)


class TestElementView
  constructor: (@model) ->

  redrawMethods : () ->
    tableBody = $("#methods")
    tableBody.empty()
    methods = @model.getAllMethods()
    updateMethod = (method) ->
      methodDescription = method.name
      if method.parameters? and method.parameters.length > 0
        methodDescription += " (" + (param.name for param in method.parameters).join(",") + ")"
      tableBody.append($('<tr>').data("method", method).append($('<td>').css("width","14px").addClass("hidden remoteMethodDragger").append($('<a>').attr({class : "icon-move"}))).append($('<td>').text(methodDescription)))
      tableBody.children("tr").draggable({revert : true, revertDuration : 0 ,helper:"clone", handle : "a"})
    updateMethod method for method in methods

  redrawRemoteMethods : () ->
    tableBody = $("#remote-methods")
    tableBody.empty()
    remoteMethods = @model.getAllRemoteMethods()
    updateMethod = (remoteMethod) ->
      methodDescription = remoteMethod.name
      if remoteMethod.parameters? and remoteMethod.parameters.length > 0
        methodDescription += " (" + (param.name for param in remoteMethod.parameters).join(",") + ")"
      tableBody.append($('<tr>').data("method", remoteMethod).append($('<td>').css("width","14px").addClass("hidden remoteMethodDragger").append($('<a>').attr({class : "icon-move"}))).append($('<td>').text(methodDescription)))
      tableBody.children("tr").draggable({revert : true, revertDuration : 0 , helper:"clone", handle : "a"})
    updateMethod remoteMethod for remoteMethod in remoteMethods

  redrawScripts : () ->
    tableBody = $("#scripts")
    tableBody.empty()
    scripts = @model.getAllScripts()
    updateScript = (script) ->
      tableBody.append($('<tr>').append($('<td>').text(script.name)))
    updateScript script for script in scripts


model = new TestElementModel()
view = new TestElementView model
detailsModel = new DetailsElementModel()
detailsView = new DetailsElementView detailsModel
detailsController = new DetailsElementController detailsModel, detailsView

onSuccessfulCreation = (data) =>
  if data.errors.length == 0
    alertMessage("success","alert-success")
    detailsController.showWelcome()
    updateMethods()
  else
    alertMessage(data.errors[0],"alert-error")

onRemoteMethodsRecieved = (data) =>
  if data.errors? and data.errors.length > 0
    alertMessage(data.errors[0].toString(),"alert-error")
  else
    model.addRemoteMethod(remoteMethod.methodDefinition) for remoteMethod in data
  view.redrawRemoteMethods()

onMethodsRecieved = (data) =>
  if data.errors? and data.errors.length > 0
    alertMessage(data.errors[0].toString(),"alert-error")
  else
    model.addMethod(method) for method in data.methodDefinitions
  view.redrawMethods()


onScriptsRecieved = (data) =>
  if data.errors? and data.errors.length > 0
    alertMessage(data.errors[0].toString,"alert-error")
  else
    model.addScript(script) for script in data.scripts
  view.redrawScripts()

alertMessage = (message, alertStyle) ->
  $("#messageContainer").empty().append($('<div>').addClass('alert ' + alertStyle).alert().append($('<button>').attr({type:"button", class:"close", "data-dismiss":"alert"}).text('x')).append($('<strong>').text(message)))

updateMethods = () ->
  message =
    type : "GET",
    url : "/remoteMethods",
    contentType : "text/json",
    dataType : 'json'
    success : onRemoteMethodsRecieved
  $.ajax(message)
  message =
    type : "GET",
    url : "/scripts",
    contentType : "text/json",
    dataType : 'json'
    success : onScriptsRecieved
  $.ajax(message)
  message =
    type : "GET",
    url : "/methods",
    contentType : "text/json",
    dataType : 'json'
    success : onMethodsRecieved
  $.ajax(message)

sendCreateScriptMessage = (data) ->
  message =
    type : "POST",
    url : "/createScript",
    data : data,
    contentType : "text/json",
    dataType : 'json'
    success : onSuccessfulCreation
  $.ajax(message)

sendCreateMethodMessage = (data) ->
  message =
    type : "POST",
    url : "/createMethod",
    data : data
    contentType : "text/json",
    dataType : 'json'
    success : onSuccessfulCreation
  $.ajax(message)

$(()->
  updateMethods()
  detailsView.redraw()
  $("#createRemoteButton").click(()-> detailsController.beginCreatingRemoteMethod())
  $("#createMethodButton").click(()-> detailsController.beginCreatingMethod())
  $("#createScriptButton").click(()-> detailsController.beginCreatingScript())
)

