jQuery.fn.filterByData = (selector, dataKey) ->
  input = this
  whitespaceReplacer = new RegExp("\\s*", "g")
  input.keyup(() ->
    filter = input.val().toLowerCase()
    filter = ".*" + filter.replace(whitespaceReplacer, ".*") + ".*"
    matcher = new RegExp(filter, "g")
    $(selector).each((index, el)->
      el = $(el)
      if !el.data(dataKey).match(matcher)
        el.hide()
      else
        el.show()
      el
    ))
  this