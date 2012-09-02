window.alertMessage = (message, alertStyle) ->
  $("#messageContainer").empty().append($('<div>').addClass('alert ' + alertStyle).alert().append($('<button>').attr({type:"button", class:"close", "data-dismiss":"alert"}).text('x')).append($('<strong>').text(message)))

$(()->
  model = new window.TestElementModel()
  view = new window.TestElementView model, $('#methods'),$('#remote-methods'),$('#scripts')
  controller = new window.TestElementController model, view
  detailsPanel = new window.DetailsPanel $("#detailsArea")
  $("#createScriptButton").click(()->$(document).trigger("show-create-script"))
  $("#createMethodButton").click(()->$(document).trigger("show-create-method"))
  $("#createRemoteMethodButton").click(()->$(document).trigger("show-create-remote-method"))
  $(document).on("new-script",()-> api.getScripts((scripts)=>controller.addScripts(scripts)))
  $(document).on("new-remote-method",()-> api.getRemoteMethods((methods)=>controller.addRemoteMethods(methods)))
  $(document).on("new-method",()-> api.getMethods((methods)=>controller.addMethods(methods)))
  api = new window.ScenarioApi
  api.getMethods((methods)=>controller.addMethods(methods))
  api.getRemoteMethods((methods)=>controller.addRemoteMethods(methods))
  api.getScripts((scripts)=>controller.addScripts(scripts))
)