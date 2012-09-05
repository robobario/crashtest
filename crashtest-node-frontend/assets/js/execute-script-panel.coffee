window.ExecuteScriptPanel = class CreateScriptPanel
  constructor: (@element, @model) ->
    if !@element? or !@model?
      throw "not enough arguments provided to the Create Script Panel"

  render: () ->
    heading = $("<h2>").text(@model.name).appendTo(@element)
    table = $("<table>").addClass("table").appendTo(@element)
    tbody = $("<tbody>").appendTo(table)

    renderInitialTable = (initialState)->
      for node,index in initialState.children
        createRows(tbody, node, [0,index]);

    createRows = (container, node, currentPath) ->
      pathString = currentPath.join(".")
      row = $("<tr>").appendTo(tbody)
      row.data("path",pathString)
      nodeDescription = node.name
      if node.parameters.length > 0
        nodeDescription += " (" + node.parameters.map((item)->item.value).join(",") + ") "
      cell = $("<td>").text(nodeDescription).appendTo(row)
      if node.completed
        row.addClass("success")
      if node.children.length > 0
        children = $()
        button = $("<a href='#'>").addClass("btn btn-success").appendTo(cell)
        for child,index in node.children
          inner = createRows(container, child, currentPath.concat([index]));
          inner.hide()
          children = children.add(inner)
        button.click(()->children.toggle())
      row

    updatePath = (path)->
      tbody.find("tr").each((index,el)->
        if $(el).data("path") == path
          $(el).addClass("success")
      )


    $(document).on("script-start",(event, initialState)=>renderInitialTable(initialState))
    $(document).on("script-execution-update",(event, completedPath)=>updatePath(completedPath))
    $(document).trigger("begin-execute-script",@model)
