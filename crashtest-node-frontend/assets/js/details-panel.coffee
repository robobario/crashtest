window.DetailsModel = class DetailsModel
  constructor: () ->
    @mode = "welcome"

  getMode: () ->
    @mode

  setMode: (mode) ->
    @mode = mode

window.DetailsPanel = class DetailsPanel
  constructor: ( @detailsContainer) ->
    @model = new window.DetailsModel()
    @welcomePanel = new window.WelcomePanel @detailsContainer
    @createScriptPanel = new window.CreateScriptPanel @detailsContainer
    @createRemoteMethodPanel = new window.CreateRemoteMethodPanel @detailsContainer
    @createMethodPanel = new window.CreateMethodPanel @detailsContainer
    $(document).on("show-create-script", ()=>
      @model.setMode("create-script")
      this.redraw()
    )
    $(document).on("show-create-method", ()=>
      @model.setMode("create-method")
      this.redraw()
    )
    $(document).on("show-create-remote-method", ()=>
      @model.setMode("create-remote-method")
      this.redraw()
    )
    $(document).on("show-welcome", ()=>
      @model.setMode("welcome")
      this.redraw()
    )

  redraw: () ->
    $(document).trigger("hide-test-element-draggers")
    mode = @model.getMode()
    switch mode
      when "welcome" then @welcomePanel.render()
      when "create-script" then @createScriptPanel.render()
      when "create-method" then @createMethodPanel.render()
      when "create-remote-method" then @createRemoteMethodPanel.render()
      else
        console.log(@model.getMode())