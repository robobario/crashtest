
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , http = require('http')
  , path = require('path')
  , io = require('socket.io')
  , request = require("request")
    , async = require("async");

var app = express();

http.globalAgent.maxSockets = 100

app.configure(function(){
  app.set('port', process.env.PORT || 3000);
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(require('connect-assets')())
  app.use(app.router);
  app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function(){
  app.use(express.errorHandler());
});

app.get('/', routes.index);
app.get('/users', user.list);

server = http.createServer(app).listen(app.get('port'), function(){
  console.log("Express server listening on port " + app.get('port'));
});

ioserver = io.listen(server);

ioserver.sockets.on('connection', function (socket) {
    socket.on("get-scripts", function(data,fn){
        getScripts(function(stuff){
            socket.emit('scripts',stuff)
        });
    });
    socket.on("get-remote-methods", function(data){
        getRemoteMethods(function(stuff){
            socket.emit('remote-methods',stuff)
        });
    });
    socket.on("get-methods", function(data){
        getMethods(function(stuff){
            socket.emit('methods',stuff)
        });
    });
    socket.on("create-script", function(data){
        createScript(data,function(stuff){
            socket.emit('script-created',stuff)
        });
    });
    socket.on("create-remote-method", function(data){
        createRemoteMethod(data,function(stuff){
            socket.emit('remote-method-created',stuff)
        });
    });
    socket.on("create-method", function(data){
        createMethod(data,function(stuff){
            socket.emit('method-created',stuff)
        });
    });
});

var getUrl = function(url,callback){
    request(url, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback(null, JSON.parse(body))
        }else{
            console.log("crap")
        }
    })
};


var createScript = function(script,callback){
    request({method:"POST", url:"http://localhost:8180/crashtest/newScript",body:JSON.stringify(script)}, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback(JSON.parse(body))
        }else{
            console.log("failed to create script")
        }
    });
};


var createRemoteMethod = function(method,callback){
    console.log(JSON.stringify(method))
    request({method:"POST", url:"http://localhost:8180/crashtest/newRemoteMethod",body:JSON.stringify(method)}, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback(JSON.parse(body))
        }else{
            console.log("failed to create method")
        }
    });
};

var createMethod = function(method,callback){
    console.log(JSON.stringify(method))
    request({method:"POST", url:"http://localhost:8180/crashtest/newMethod",body:JSON.stringify(method)}, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            callback(JSON.parse(body))
        }else{
            console.log("failed to create method")
        }
    });
};


var getScripts = function(callback){
    var req = getUrl("http://localhost:8180/crashtest/scripts", function(err, data){
        if(data != null && typeof data.allScriptDetailsUrls !== "undefined" && data.allScriptDetailsUrls.length > 0){
            async.mapSeries(data.allScriptDetailsUrls.map(function(url){return "http://localhost:8180/"+url}), getUrl, function(err, results){
                scripts = results.map(function(result){return result.script});
                callback(scripts);
            })
        }
    });
};

var getRemoteMethods = function(callback){
    var req = getUrl("http://localhost:8180/crashtest/remote-methods", function(err, data){
        if(data != null &&  data.remoteMethodDetails != null && data.remoteMethodDetails.length > 0){
            async.mapSeries(data.remoteMethodDetails.map(function(url){return "http://localhost:8180/"+url}), getUrl, function(err, results){
                remoteMethods = results.map(function(result){return result.methodDefinition});
                callback(remoteMethods);
            })
        }
    })
};

var getMethods = function(callback){
    var req = getUrl("http://localhost:8180/crashtest/methods", function(err, data){
        if(data != null &&  data.methodDetails != null && data.methodDetails.length > 0){
            async.mapSeries(data.methodDetails.map(function(url){return "http://localhost:8180/"+url}), getUrl, function(err, results){
                methods = results.map(function(result){return result.methodDefinition});
                callback(methods);
            })
        }
    })
};
