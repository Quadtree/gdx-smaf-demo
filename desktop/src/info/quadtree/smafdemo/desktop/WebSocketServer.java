package info.quadtree.smafdemo.desktop;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/smafserver")
public class WebSocketServer {

    Map<String, Long> sessionToPlayerId = new HashMap<>();

    @OnMessage
    public void messageReceived(String msg, Session sess){
        //System.out.println("msg=" + msg + " sess=" + sess + " sessId=" + sess.getId());


    }
}
