window.CreateScriptPanel = class CreateScriptPanel
  constructor: (@element) ->
    if(!@element?)
      throw "not enough arguments provided to the Create Script Panel"

  render: () ->
    $(document).trigger("show-test-element-draggers")
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
      message = {name:scriptName, statementRequests : $("#scriptStatementTable > tr").map(toMessage).toArray()}
      $(document).trigger("create-script",message)
    doCreate = () ->
      try
        create()
      catch error
        window.alertMessage(error.toString(), "alert-error")

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
    @element.empty().append($(outerDiv))
    $(".remoteMethodDragger").removeClass("hidden")