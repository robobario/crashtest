window.ScenarioApi = class ScenarioApi
  constructor: () ->
    @socket = io.connect('http://localhost')
    ;
    $(document).on('create-script', (event, script) =>
      this.createScript(script, (data)->
        onCreation("new-script", data)
      )
    )
    $(document).on('create-remote-method', (event, method) =>
      this.createRemoteMethod(method, (data)->
        onCreation("new-remote-method", data)
      )
    )
    $(document).on('create-method', (event, method) =>
      this.createMethod(method, (data)->
        onCreation("new-method", data)
      )
    )
    $(document).on('begin-execute-script', (event, scriptAndCallbacks) =>
      onScriptStart = (data) ->  scriptAndCallbacks.startCallback(data)
      onScriptUpdate = (data) ->  scriptAndCallbacks.updateCallback(data)
      this.beginScriptExecution(scriptAndCallbacks.script,onScriptStart,onScriptUpdate)
    )

  onCreation = (type, data) ->
    if data.errors.length == 0
      window.alertMessage("success", "alert-success")
      $(document).trigger("show-welcome")
      $(document).trigger(type)
    else
      window.alertMessage(data.errors[0], "alert-error")


  getMethods: (callback) ->
    @socket.on('methods', (data) =>
      callback(data)
    )
    @socket.emit("get-methods")

  getRemoteMethods: (callback) ->
    @socket.on('remote-methods', (data) =>
      callback(data)
    )
    @socket.emit("get-remote-methods")

  getScripts: (callback) ->
    @socket.on('scripts', (data) =>
      callback(data)
    )
    @socket.emit("get-scripts")

  createScript: (script, callback) ->
    @socket.on('script-created', (data) =>
      callback(data)
    )
    @socket.emit("create-script", script)

  createRemoteMethod: (method, callback) ->
    @socket.on('remote-method-created', (data) =>
      callback(data)
    )
    @socket.emit("create-remote-method", method)

  createMethod: (method, callback) ->
    @socket.on('method-created', (data) =>
      callback(data)
    )
    @socket.emit("create-method", method)

  beginScriptExecution: (script, startCallback, updateCallback) ->
    @socket.on('script-started', (data) =>
      startCallback(data)
    )
    @socket.on('script-execution-update', (data) =>
      updateCallback(data)
    )
    @socket.emit("execute-script", script)
