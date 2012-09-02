assert = buster.assert

buster.testCase 'test element model',
  setUp: ()->
    this.model = new window.TestElementModel

  'is empty on construction': ->
    assert(this.model.getAllScripts().length == 0)
    assert(this.model.getAllRemoteMethods().length == 0)
    assert(this.model.getAllMethods().length == 0)

  'can not have empty scripts added to it': ->
    this.model.addScript({})
    assert(this.model.getAllScripts().length == 0)

  'can not have nameless scripts added to it': ->
    model = new window.TestElementModel
    this.model.addScript({"executionUrl": "url"})
    assert(this.model.getAllScripts().length == 0)

  'can not have scripts with no execution url added to it': ->
    model = new window.TestElementModel
    this.model.addScript({"name": "name"})
    assert(this.model.getAllScripts().length == 0)

  'can have valid scripts added to it': ->
    script = {"name": "theName", "executionUrl": "theUrl"}
    this.model.addScript(script)
    assert.equals(this.model.getAllScripts().length, 1)
    assert.equals(this.model.getAllScripts()[0], script)

  'throws away the old script when a new script with the same name is added': ->
    script = {"name": "theName", "executionUrl": "theUrl"}
    script2 = {"name": "theName", "executionUrl": "theUrlul"}
    this.model.addScript(script)
    this.model.addScript(script2)
    assert.equals(1, this.model.getAllScripts().length)
    assert.equals(script2, this.model.getAllScripts()[0])

  'can not add method with no name': ->
    method = {"parameters": [], "statements": []}
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unnamed parameter - no name': ->
    method = {name: "name", "parameters": [
      {}
    ], "statements": []}
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unnamed parameter - zero length': ->
    method = {name: "name", "parameters": [
      {"name": ""}
    ], "statements": []}
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unnamed statement - no name': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [], "@type": "remote invocation"}
      ]
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unnamed statement - zero length': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [], "@type": "remote invocation", "methodName": ""}
      ]
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unknown type': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [], "@type": "bad value", "methodName": "name"}
      ]
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unnamed statement parameter': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unnamed statement parameter - zero length': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": ""}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can not add method with unknown expression type in a statement': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "random", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    assert.equals(0, this.model.getAllMethods().length)

  'can add valid method': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    assert.equals(1, this.model.getAllMethods().length)
    assert.equals(method, this.model.getAllMethods()[0])

  'throws away old method when a new method with the same name is added': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    method2 =
      name: "name"
      parameters: [
        {"name": "nsd"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adasd"}
        ], "@type": "method invocation", "methodName": "named"}
      ]
    this.model.addMethod(method)
    this.model.addMethod(method2)
    assert.equals(1, this.model.getAllMethods().length)
    assert.equals(method2, this.model.getAllMethods()[0])

  'can not have unnamed remote methods added - not present': ->
    remoteMethod =
      parameters: [
        {"name": "n"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    assert(this.model.getAllRemoteMethods().length == 0)

  'can not have unnamed remote methods added - zero length': ->
    remoteMethod =
      name: ""
      parameters: [
        {"name": "n"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    assert(this.model.getAllRemoteMethods().length == 0)

  'can not have remote methods added with no parameter array': ->
    remoteMethod =
      name: "name"
    this.model.addRemoteMethod(remoteMethod)
    assert(this.model.getAllRemoteMethods().length == 0)

  'can not have remote methods added with unnamed parameter - not present': ->
    remoteMethod =
      name: "name"
      parameters: [
        {}
      ]
    this.model.addRemoteMethod(remoteMethod)
    assert(this.model.getAllRemoteMethods().length == 0)

  'can not have remote methods added with unnamed parameter - zero length': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": ""}
      ]
    this.model.addRemoteMethod(remoteMethod)
    assert(this.model.getAllRemoteMethods().length == 0)

  'can have valid remote methods added': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": "jones"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    assert.equals(1, this.model.getAllRemoteMethods().length)
    assert.equals(remoteMethod, this.model.getAllRemoteMethods()[0])

  'throws away the old remote method when a new method with the same name is added': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": "jones"}
      ]
    remoteMethod2 =
      name: "name"
      parameters: [
        {"name": "jonesdddd"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.model.addRemoteMethod(remoteMethod2)
    assert.equals(1, this.model.getAllRemoteMethods().length)
    assert.equals(remoteMethod2, this.model.getAllRemoteMethods()[0])

buster.testCase 'test element view',
  setUp: ()->
    this.model = new window.TestElementModel
    this.scriptContainer = $("<div>")
    this.methodContainer = $("<div>")
    this.remoteMethodContainer = $("<div>")
    this.view = new window.TestElementView this.model, this.methodContainer, this.remoteMethodContainer, this.scriptContainer

  'redraw scripts emptys script container if there are no scripts': ->
    this.scriptContainer.append($("<div>"))
    this.view.redrawScripts()
    assert.equals(0, this.scriptContainer.children().length)

  'redraw scripts appends to the script container': ->
    scriptName = "theName"
    script = {"name": scriptName, "executionUrl": "theUrl"}
    this.model.addScript(script)
    this.view.redrawScripts()
    assert.equals(1, this.scriptContainer.children().length)
    td = this.scriptContainer.find("tr > td")
    assert.equals(1, td.length)
    assert.equals(scriptName, td.text())

  'redraw scripts appends many scripts to the script container': ->
    scriptName = "theName"
    scriptName2 = "otherName"
    script = {"name": scriptName, "executionUrl": "theUrl"}
    script2 = {"name": scriptName2, "executionUrl": "theUrl"}
    this.model.addScript(script)
    this.model.addScript(script2)
    this.view.redrawScripts()
    assert.equals(2, this.scriptContainer.children().length)
    td = this.scriptContainer.find("tr > td")
    names = td.map(()->$(this).text()).toArray()
    assert.equals([scriptName, scriptName2], names)

  'redraw remote methods emptys remote method container if there are no remote methods in model': ->
    this.remoteMethodContainer.append($("<div>"))
    this.view.redrawRemoteMethods()
    assert.equals(0, this.remoteMethodContainer.children().length)

  'redraw remote method appends to the remote method container': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": "jones"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.view.redrawRemoteMethods()
    tr = this.remoteMethodContainer.find("tr")
    assert.equals(1, tr.length)

  'redraw remote method attaches the method as data to the drawn tr': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": "jones"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.view.redrawRemoteMethods()
    tr = this.remoteMethodContainer.find("tr")
    assert.equals(remoteMethod, tr.data("method"))

  'redraw remote method creates a td to contain the drag and drop icon': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": "jones"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.view.redrawRemoteMethods()
    td = this.remoteMethodContainer.find("tr > td").first()
    assert(td.hasClass("hidden"))
    assert(td.hasClass("methodDragger"))
    assert.equals(1, td.children("a").length)
    assert(td.children("a").hasClass("icon-move"))

  'redraw remote method creates a td that contains the remote method description': ->
    remoteMethod =
      name: "name"
      parameters: [
        {name: "jones"},
        {name: "age"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.view.redrawRemoteMethods()
    td = this.remoteMethodContainer.find("tr > td").last()
    assert.equals("name (jones,age)", td.text())

  'redraw remote method makes the new tr draggable': ->
    remoteMethod =
      name: "name"
      parameters: [
        {name: "jones"},
        {name: "age"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.view.redrawRemoteMethods()
    tr = this.remoteMethodContainer.find("tr").first()
    assert(tr.is('.ui-draggable'))

  'redraw methods emptys method container if there are no methods in model': ->
    this.methodContainer.append($("<div>"))
    this.view.redrawMethods()
    assert.equals(0, this.methodContainer.children().length)

  'redraw method appends to the method container': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    this.view.redrawMethods()
    tr = this.methodContainer.find("tr")
    assert.equals(1, tr.length)

  'redraw method attaches the method as data to the drawn tr': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    this.view.redrawMethods()
    tr = this.methodContainer.find("tr")
    assert.equals(method, tr.data("method"))

  'redraw method creates a td to contain the drag and drop icon': ->
    method =
      name: "name"
      parameters: [
        {"name": "n"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    this.view.redrawMethods()
    td = this.methodContainer.find("tr > td").first()
    assert(td.hasClass("hidden"))
    assert(td.hasClass("methodDragger"))
    assert.equals(1, td.children("a").length)
    assert(td.children("a").hasClass("icon-move"))

  'redraw method creates a td that contains the method description': ->
    method =
      name: "name"
      parameters: [
        {"name": "respect"},
        {"name": "age"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    this.view.redrawMethods()
    td = this.methodContainer.find("tr > td").last()
    assert.equals("name (respect,age)", td.text())


  'redraw method makes the new tr draggable': ->
    method =
      name: "name"
      parameters: [
        {"name": "respect"},
        {"name": "age"}
      ]
      statements: [
        {parameterExpressions: [
          {"@type": "identifier", "identifierName": "adas"}
        ], "@type": "method invocation", "methodName": "name"}
      ]
    this.model.addMethod(method)
    this.view.redrawMethods()
    tr = this.methodContainer.find("tr").first()
    assert(tr.is('.ui-draggable'))

  'removes the hidden class from draggers when a document.show-test-element-draggers event is recieved': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": "jones"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.view.redrawRemoteMethods()
    $(document).trigger('show-test-element-draggers')
    td = this.remoteMethodContainer.find("tr > td").first()
    assert(!td.hasClass("hidden"))

  'adds the hidden class from draggers when a document.hide-test-element-draggers event is recieved': ->
    remoteMethod =
      name: "name"
      parameters: [
        {"name": "jones"}
      ]
    this.model.addRemoteMethod(remoteMethod)
    this.view.redrawRemoteMethods()
    $(document).trigger('show-test-element-draggers')
    $(document).trigger('hide-test-element-draggers')
    td = this.remoteMethodContainer.find("tr > td").first()
    assert(td.hasClass("hidden"))



