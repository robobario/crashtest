class TestElementValidator
  constructor: () ->

  areParamsValid = (params) ->
    isParamInvalid = (param) ->
      !param.name? or param.name.length == 0
    (param for param in params when isParamInvalid(param)).length == 0

  isStatementExpressionInvalid = (expression) ->
    isValidType = () -> expression["@type"] == "identifier" or expression["@type"] == "literal"
    if expression["@type"]? and isValidType()
      switch expression["@type"]
        when "identifier"
          return !expression.identifierName? or expression.identifierName.length == 0
        when "literal"
          return !expression.value? or expression.value.length == 0
    else
      return true
    !expression.identifierName? or expression.identifierName.length == 0 or !expression["@type"]? or !isValidType()

  isStatementInvalid = (statement) ->
    statementTypeIncorrect = () -> (statement["@type"] != "remote invocation" and statement["@type"] != "method invocation")
    anExpressionIsIncorrect = () -> (expression for expression in statement.parameterExpressions when isStatementExpressionInvalid(expression)).length > 0
    !statement.methodName? or statement.methodName.length == 0 or statementTypeIncorrect() or anExpressionIsIncorrect()

  areStatementsValid = (statements)->
    (statement for statement in statements when isStatementInvalid(statement)).length == 0

  isScriptValid: (script) ->
    script.name? and script.executionUrl?

  isMethodValid: (method) ->
    method.name? and method.name.length > 0 and method.parameters? and method.statements? and areParamsValid(method.parameters) and areStatementsValid(method.statements)

  isRemoteMethodValid: (remoteMethod) ->
    remoteMethod.name? and remoteMethod.name.length > 0 and remoteMethod.parameters? and areParamsValid(remoteMethod.parameters)


window.TestElementModel = class TestElementModel
  constructor: () ->
    @remoteMethods = {}
    @methods = {}
    @scripts = {}
    @validator = new TestElementValidator()

  addScript: (script) ->
    if @validator.isScriptValid(script)
      @scripts[script.name] = script

  getAllScripts: () ->
    (value for prop, value of  @scripts)

  addRemoteMethod: (remoteMethod) ->
    if @validator.isRemoteMethodValid(remoteMethod)
      remoteMethod.statementType = "remote invocation"
      @remoteMethods[remoteMethod.name] = remoteMethod

  getAllRemoteMethods: () ->
    (value for prop, value of  @remoteMethods)

  addMethod: (method) ->
    if @validator.isMethodValid(method)
      method.statementType = "method invocation"
      @methods[method.name] = method

  getAllMethods: () ->
    (value for prop, value of  @methods)

window.TestElementView = class TestElementView
  constructor: (@model, @methodContainer, @remoteMethodContainer, @scriptContainer) ->
    $(document).bind('show-test-element-draggers',this.showDraggers)
    $(document).bind('hide-test-element-draggers',this.hideDraggers)
    if !@model? or !@methodContainer? or !@remoteMethodContainer or !@scriptContainer
      throw "not enough args provided to constructor"

  showDraggers : () =>
    @methodContainer.find(".methodDragger").removeClass("hidden")
    @remoteMethodContainer.find(".methodDragger").removeClass("hidden")

  hideDraggers : () =>
    @methodContainer.find(".methodDragger").addClass("hidden")
    @remoteMethodContainer.find(".methodDragger").addClass("hidden")

  redrawMethods : () ->
    tableBody = @methodContainer
    tableBody.empty()
    methods = @model.getAllMethods()
    updateMethod = (method) ->
      methodDescription = method.name
      if method.parameters? and method.parameters.length > 0
        methodDescription += " (" + (param.name for param in method.parameters).join(",") + ")"
      tableBody.append($('<tr>').data("method", method).append($('<td>').css("width","14px").addClass("hidden methodDragger").append($('<a>').attr({class : "icon-move"}))).append($('<td>').text(methodDescription)))
      tableBody.children("tr").draggable({revert : true, revertDuration : 0 ,helper:"clone", handle : "a"})
    updateMethod method for method in methods

  redrawRemoteMethods : () ->
    tableBody = @remoteMethodContainer
    tableBody.empty()
    remoteMethods = @model.getAllRemoteMethods()
    updateMethod = (remoteMethod) ->
      methodDescription = remoteMethod.name
      if remoteMethod.parameters? and remoteMethod.parameters.length > 0
        methodDescription += " (" + (param.name for param in remoteMethod.parameters).join(",") + ")"
      tableBody.append($('<tr>').data("method", remoteMethod).append($('<td>').css("width","14px").addClass("hidden methodDragger").append($('<a>').attr({class : "icon-move"}))).append($('<td>').text(methodDescription)))
      tableBody.children("tr").draggable({revert : true, revertDuration : 0 , helper:"clone", handle : "a"})
    updateMethod remoteMethod for remoteMethod in remoteMethods

  redrawScripts : () ->
    @scriptContainer.empty()
    scripts = @model.getAllScripts()
    updateScript = (script) =>
      @scriptContainer.append($('<tr>').append($('<td>').text(script.name)))
    updateScript script for script in scripts

window.TestElementController = class TestElementController
  constructor: (@model,@view) ->

  addScripts: (scripts) ->
    @model.addScript script for script in scripts
    @view.redrawScripts()

  addRemoteMethods: (remoteMethods) ->
    @model.addRemoteMethod method for method in remoteMethods
    @view.redrawRemoteMethods()

  addMethods: (methods) ->
    @model.addMethod method for method in methods
    @view.redrawMethods()
