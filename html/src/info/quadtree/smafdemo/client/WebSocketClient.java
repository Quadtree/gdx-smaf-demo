package info.quadtree.smafdemo.client;

import info.quadtree.smafdemo.DemoActorContainer;
import info.quadtree.smafdemo.smaf.ActorContainer;

public class WebSocketClient {
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

    public void update(){
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

            while(System.currentTimeMillis() > updateTimeDone) {
                container.update();
                updateTimeDone += 16;
            }
        }
    }

    public boolean isConnected(){
        return getWebSocketStatus() == WS_STATUS_OPEN;
    }

    public ActorContainer getContainer(){
        return container;
    }

    private static native int getWebSocketStatus() /*-{
        if (clientWebSocket){
            return clientWebSocket.readyState;
        } else {
            return -1;
        }
    }-*/;

    private static native int startConnection() /*-{
        clientWebSocket = new WebSocket("ws://" + location.host + "/smafserver");
    }-*/;
}
