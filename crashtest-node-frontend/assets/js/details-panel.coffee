window.DetailsModel = class DetailsModel
  constructor: (@currentPanel) ->
    @previousPanel = @currentPanel

  getCurrentPanel: () ->
    @currentPanel

  getPreviousPanel: () ->
    @previousPanel

  setCurrentPanel: (currentPanel) ->
    @previousPanel = @currentPanel
    @currentPanel = currentPanel

window.DetailsPanel = class DetailsPanel
  constructor: ( @detailsContainer) ->
    @welcomePanel = new window.WelcomePanel @detailsContainer
    @model = new window.DetailsModel(@welcomePanel)
    @createScriptPanel = new window.CreateScriptPanel @detailsContainer
    @createRemoteMethodPanel = new window.CreateRemoteMethodPanel @detailsContainer
    @createMethodPanel = new window.CreateMethodPanel @detailsContainer

    $(document).on("show-create-script", ()=>
      @model.setCurrentPanel(@createScriptPanel)
      this.redraw()
    )
    $(document).on("show-create-method", ()=>
      @model.setCurrentPanel(@createMethodPanel)
      this.redraw()
    )
    $(document).on("show-create-remote-method", ()=>
      @model.setCurrentPanel(@createRemoteMethodPanel)
      this.redraw()
    )
    $(document).on("execute-script", (event, data)=>
      @model.setCurrentPanel(new window.ExecuteScriptPanel @detailsContainer,data)
      this.redraw()
    )
    $(document).on("show-welcome", ()=>
      window.ExecuteScriptPanel
      @model.setCurrentPanel(@welcomePanel)
      this.redraw()
    )

  redraw: () ->
    @detailsContainer.empty()
    $(document).trigger("hide-test-element-draggers")
    oldPanel = @model.getPreviousPanel()
    $(document).off("script-start")
    $(document).off("script-execution-update")
    @model.getCurrentPanel().render();