package info.quadtree.smafdemo.client;

import com.badlogic.gdx.Gdx;
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

    public void send(RPCMessage message){
        Json js = new Json();
        sendText(js.toJson(message));
    }

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
                container.setRpcMessageSender(this::send);

                Gdx.app.log("SMAF", "Created container, sending greeting");

                RPCMessage greetingMessage = new RPCMessage();
                greetingMessage.setGreeting(true);
                send(greetingMessage);

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
        if (typeof($wnd.clientWebSocket) == 'undefined') $wnd.clientWebSocket = null;
        if (typeof($wnd.messages) == 'undefined') $wnd.messages = null;
    }-*/;

    private static native String getNextMessage() /*-{
        if ($wnd.messages.length > 0){
            return $wnd.messages.shift();
        }
        return null;
    }-*/;

    private static native int getWebSocketStatus() /*-{
        if ($wnd.clientWebSocket){
            return $wnd.clientWebSocket.readyState;
        } else {
            return -1;
        }
    }-*/;

    private static native int startConnection() /*-{
        console.log("startConnection() called");
        $wnd.clientWebSocket = new WebSocket("ws://" + location.host + "/smafserver");
        $wnd.messages = [];
        $wnd.clientWebSocket.onmessage = function(msg){
            $wnd.messages.push(msg);
        }
    }-*/;

    private static native void sendText(String text) /*-{
        $wnd.clientWebSocket.send(text);
    }-*/;
}
