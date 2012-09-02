window.CreateMethodPanel = class CreateMethodPanel
  constructor: (@element) ->
    if(!@element?)
      throw "not enough arguments provided to the Create Method Panel"

  render: () ->
    $(document).trigger("show-test-element-draggers")
    onParamDelete = (param, input) ->
      input = $(input)
      if input.attr("expression-type") == "identifier" and input.val() == param
        input.next("a").click()

    $(document).on("parameter-removed", (event, param)->
      $(".statement > td > div > input").each((index, el)->onParamDelete(param, el))
    )

    onDrop = (event, ui) ->
      method = ui.draggable.data("method")
      if method?
        onParamDrop = (event, ui) ->
          param = ui.draggable.data("param")
          if param?
            input = $(this)
            deleteBtn = $("<a>").attr("href","#").addClass("icon-minus-sign")
            onDelete = () =>
              input.removeAttr("disabled").val("").attr({"placeholder" : param.name, "expression-type" : "literal"})
              deleteBtn.remove()
            deleteBtn.click(onDelete)
            input.attr('disabled':'disabled',"expression-type" : "identifier").val(param).after(deleteBtn)
        tr = $("<tr>").data("method",method).addClass("statement")
        td = $("<td>").text(method.name + " ").appendTo(tr)
        appendInput = (element, paramName) ->
          container = $("<div>").addClass("input-append dropdown").appendTo(element)
          button = $("<input>").droppable({drop : onParamDrop, tolerance:"pointer"}).addClass('input-small').attr({"placeholder" : paramName, "type" : "text", "expression-type" : "literal"})
          container.append(button)
        appendInput(td, param.name) for param in method.parameters
        $("#methodStatementTable").append(tr)


    outerDiv = $("<div>").addClass("well")
    form = $("<form>").addClass("form-horizontal").appendTo(outerDiv)
    legend = $("<legend>").text("Create A Method").appendTo(form)
    nameComponent = $("<div>").addClass("control-group").appendTo(form)
    nameLabel = $("<label>").attr({class:"control-label" ,for:"methodNameInput"}).text("Name").appendTo(nameComponent)
    nameInputDiv = $("<div>").addClass("controls").appendTo(nameComponent)
    nameInput = $("<input>").attr({placeholder:"Method Name", id:"methodNameInput", type:"text"}).appendTo(nameInputDiv)

    newParamContainer = $("<div>").attr({id:"new-param-container"}).appendTo(form)
    parameterPanel = new window.CreateParameterPanel newParamContainer
    parameterPanel.render()

    legend = $("<legend>").text("Statements").appendTo(form)
    dropzone = $("<div>").droppable({drop : onDrop, tolerance : "pointer"}).addClass("dropZone").text("drag and drop methods and remote methods on me").appendTo(form)
    droptable = $("<table>").addClass("table table-striped").appendTo(form)
    dropbody = $("<tbody>").attr({id:"methodStatementTable"}).appendTo(droptable)

    createComponent = $("<div>").addClass("control-group").appendTo(form)
    createInputDiv = $("<div>").addClass("controls").appendTo(createComponent)

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
      parameters = ({"name" : param} for param in parameterPanel.getParameters())
      $(document).trigger("create-method",{name:methodName,parameters:parameters, statements : $("#methodStatementTable > tr").map(toMessage).toArray()})
    doCreate = () ->
      try
        create()
      catch error
        alertMessage(error.toString(), "alert-error")
    createButton = $("<a>").click(doCreate).addClass("btn btn-primary").attr({href:"#"}).text("Create").appendTo(createInputDiv)
    @element.empty().append($(outerDiv))