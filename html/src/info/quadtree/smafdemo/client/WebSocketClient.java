package info.quadtree.smafdemo.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import info.quadtree.smafdemo.DemoActorContainer;
import info.quadtree.smafdemo.smaf.Actor;
import info.quadtree.smafdemo.smaf.ActorContainer;
import info.quadtree.smafdemo.smaf.ContainerClient;
import info.quadtree.smafdemo.smaf.RPCMessage;

import java.util.Objects;

public class WebSocketClient extends ContainerClient {
    private ActorContainer factory(){
        return new DemoActorContainer();
    }

    ActorContainer container;

    private long updateTimeDone;

    private int myId = -1;

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

        Gdx.app.setLogLevel(Application.LOG_INFO);

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
                Gdx.app.log("SMAF", "Processing incoming message: " + nextMsg);
                try {
                    Json js = new Json();
                    RPCMessage rpcMessage = js.fromJson(RPCMessage.class, nextMsg);
                    if (rpcMessage != null) {
                        if (!Objects.equals(rpcMessage.getGreeting(), true)) {
                            Actor actor = container.getActorById(rpcMessage.getTargetActor());

                            // because we're the client, we always assume that any object the server references exists
                            if (actor == null){
                                actor = container.createBlankActor(rpcMessage.getActorType());
                            }

                            actor.executeRPC(rpcMessage, "Client");
                        } else {
                            myId = rpcMessage.getTargetPlayerId();
                            Gdx.app.log("SMAF", "Server has set my id to " + myId);
                        }
                    } else {
                        Gdx.app.log("SMAF", "Message sent from client was not a valid RPC");
                    }
                } catch (Throwable t){
                    Gdx.app.log("SMAF", "Error processing message: " + t);
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
            $wnd.messages.push(msg.data);
        }
    }-*/;

    private static native void sendText(String text) /*-{
        $wnd.clientWebSocket.send(text);
    }-*/;
}
