package org.crashtest.http;

import org.crashtest.http.resources.*;
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
        router.attach("/methods/{method-id}", MethodDetailsResource.class);
        router.attach("/newRemoteMethod", RemoteMethodDefinitionResource.class);
        router.attach("/remote-methods/{remote-method-id}", RemoteMethodDetailsResource.class);
        router.attach("/newScript", ScriptDefinitionResource.class);
        router.attach("/script/{script-id}", ScriptDetailsResource.class);
        router.attach("/script/{script-id}/execute", ScriptInvocationResource.class);
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