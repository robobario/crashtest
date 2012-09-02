assert = buster.assert

buster.testCase 'welcome panel',
  setUp: ()->
    this.container = $("<div>")
    this.panel = new window.WelcomePanel this.container

  'render draws a welcome message': ->
    this.panel.render()
    assert.equals(1,this.container.children("div").length)
    div = this.container.children("div").first()
    assert(div.is(".hero-unit"))
    assert.equals(1,div.children("h1").length)
    h1 = div.children("h1").first()
    assert(h1.text().length > 0)
    assert.equals(1,div.children("p").length)
    para = div.children("p").first()
    assert(para.text().length > 0)
