onSuccess = (data) =>
  render = (script) =>
    scriptName = script.name
    $("#overview").append($('<div>').addClass('row').append($('<div>').addClass('span12').append($('<div>').addClass('well').append($('<span>').text(scriptName)))))

  if data.errors? and data.errors.length > 0
    alertMessage(data.errors[0].toString,"alert-error")
  else
    render script for script in data.scripts

alertMessage = (message, alertStyle) ->
  $("#messageContainer").append($('<div>').addClass('alert ' + alertStyle).alert().append($('<button>').attr({type:"button", class:"close", "data-dismiss":"alert"}).text('x')).append($('<strong>').text(message)))


$(()->
  message =
    type : "GET",
    url : "/scripts",
    contentType : "text/json",
    dataType : 'json'
    success : onSuccess
  $.ajax(message)
)
