package info.quadtree.smafdemo.desktop;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/smafserver")
public class WebSocketServer {
    @OnMessage
    public void testThing(String msg, Session sess){
        System.out.println("msg=" + msg + " sess=" + sess);
    }
}
