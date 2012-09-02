window.WelcomePanel = class WelcomePanel
  constructor: (@element) ->
    if(!@element?)
      throw "not enough arguments provided to the Welcome Panel"

  render: () ->
    @element.empty()
    div = $("<div>").addClass("hero-unit").appendTo(@element)
    heading = $('<h1>').text("Get Testing").appendTo(div)
    text = $('<p>').text("Define remote methods, call them from methods, arrange them into scripts").appendTo(div)
