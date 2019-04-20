package info.quadtree.smafdemo.client;

import com.badlogic.gdx.utils.Json;
import info.quadtree.smafdemo.DemoActorContainer;
import info.quadtree.smafdemo.smaf.Actor;
import info.quadtree.smafdemo.smaf.ActorContainer;
import info.quadtree.smafdemo.smaf.ContainerClient;
import info.quadtree.smafdemo.smaf.RPCMessage;

public class WebSocketClient extends ContainerClient {
    private ActorContainer factory(){
        return new DemoActorContainer();
    }

    ActorContainer container;

    private long updateTimeDone;

    private static final int WS_STATUS_UNKNOWN = -1;
    private static final int WS_STATUS_CONNECTING = 0;
    private static final int WS_STATUS_OPEN = 1;
    private static final int WS_STATUS_CLOSING = 2;
    private static final int WS_STATUS_CLOSED = 3;

    @Override
    public void update(){
        init();

        int wss = getWebSocketStatus();
        if (wss == WS_STATUS_UNKNOWN || wss == WS_STATUS_CLOSING || wss == WS_STATUS_CLOSED){
            container = null;
            startConnection();
        }

        if (getWebSocketStatus() == WS_STATUS_OPEN){
            if (container == null){
                container = factory();
                updateTimeDone = System.currentTimeMillis();
            }

            String nextMsg;
            while((nextMsg = getNextMessage()) != null){
                try {
                    Json js = new Json();
                    RPCMessage rpcMessage = js.fromJson(RPCMessage.class, nextMsg);
                    if (rpcMessage != null) {

                        Actor actor = container.getActorById(rpcMessage.getTargetActor());
                        if (actor != null) {
                            actor.executeRPC(rpcMessage, "Client");
                        } else {
                            System.err.println("RPC received for non-existant actor " + rpcMessage.getTargetActor());
                        }
                    } else {
                        System.err.println("Message sent from client was not a valid RPC");
                    }
                } catch (Throwable t){
                    System.err.println("Error processing message: " + t);
                }
            }

            while(System.currentTimeMillis() > updateTimeDone) {
                container.update();
                updateTimeDone += 16;
            }
        }
    }

    @Override
    public ActorContainer getContainer(){
        return container;
    }

    private static native String init() /*-{
        console.log("init() called");
        var clientWebSocket = null;
        if (typeof(messages) == 'undefined') var messages = null;
    }-*/;

    private static native String getNextMessage() /*-{
        if (messages.length > 0){
            return messages.shift();
        }
        return null;
    }-*/;

    private static native int getWebSocketStatus() /*-{
        if (clientWebSocket){
            return clientWebSocket.readyState;
        } else {
            return -1;
        }
    }-*/;

    private static native int startConnection() /*-{
        console.log("startConnection() called");
        clientWebSocket = new WebSocket("ws://" + location.host + "/smafserver");
        messages = [];
        clientWebSocket.onmessage = function(msg){
            messages.push(msg);
        }
    }-*/;
}
