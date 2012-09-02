window.CreateRemoteMethodPanel = class CreateRemoteMethodPanel
  constructor: (@element) ->
    if(!@element?)
      throw "not enough arguments provided to the Create Remote Method Panel"

  render: () ->
    outerDiv = $("<div>").addClass("well")
    form = $("<form>").addClass("form-horizontal").appendTo(outerDiv)
    legend = $("<legend>").text("Create A Remote Method").appendTo(form)
    nameComponent = $("<div>").addClass("control-group").appendTo(form)
    nameLabel = $("<label>").attr({class:"control-label" ,for:"inputName"}).text("Name").appendTo(nameComponent)
    nameInputDiv = $("<div>").addClass("controls").appendTo(nameComponent)
    nameInput = $("<input>").attr({placeholder:"Remote Method Name", id:"inputName", type:"text"}).appendTo(nameInputDiv)

    newParamContainer = $("<div>").attr({id:"new-param-container"}).appendTo(form)

    parameters = new window.CreateParameterPanel newParamContainer
    parameters.render()

    createComponent = $("<div>").addClass("control-group").appendTo(form)
    createInputDiv = $("<div>").addClass("controls").appendTo(createComponent)
    createButton = $("<a>").addClass("btn btn-primary").attr({href:"#"}).text("Create").appendTo(createInputDiv)
    @element.empty().append($(outerDiv))
    onCreate = () =>
      methodName = $("#inputName").val()
      if !methodName? or methodName.length == 0
        window.alertMessage("Could not create method with no name","alert-error")
      else
        data =
          name : methodName
          parameters : ({name:param} for param in parameters.getParameters())
        $(document).trigger("create-remote-method",data)
    createButton.click(onCreate)