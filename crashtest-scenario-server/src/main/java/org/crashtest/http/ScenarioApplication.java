package org.crashtest.http;

import org.crashtest.http.resources.MethodDefinitionResource;
import org.crashtest.http.resources.RemoteMethodDefinitionResource;
import org.crashtest.http.resources.ScriptDefinitionResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class ScenarioApplication extends Application {
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/newMethod", MethodDefinitionResource.class);
        router.attach("/newRemoteMethod", RemoteMethodDefinitionResource.class);
        router.attach("/newScript", ScriptDefinitionResource.class);
        return router;
    }

    public static void main(String[] args) throws Exception {
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8180);
        component.getDefaultHost().attach("/crashtest",
                new ScenarioApplication());
        component.start();
    }
}
