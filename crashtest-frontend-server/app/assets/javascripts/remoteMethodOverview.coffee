onSuccess = (data) =>
  render = (method) =>
    def =  method.methodDefinition
    methodDescription = def.name
    if def.parameters? and def.parameters.length > 0
      methodDescription += " (" + (param.name for param in def.parameters).join(",") + ")"
    $("#overview").append($('<div>').addClass('row').append($('<div>').addClass('span12').append($('<div>').addClass('well').append($('<span>').text(methodDescription)))))
  render remoteMethod for remoteMethod in data

$(()->
  message =
    type : "GET",
    url : "/remoteMethods",
    contentType : "text/json",
    dataType : 'json'
    success : onSuccess
  $.ajax(message)
)
