package info.quadtree.smafdemo.desktop;

import info.quadtree.smafdemo.DemoActorContainer;
import info.quadtree.smafdemo.SMAFDemo;
import info.quadtree.smafdemo.smaf.ActorContainer;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.backends.headless.HeadlessApplication;

@ServerEndpoint("/smafserver")
public class WebSocketServer {
    private ActorContainer factory(){
        return new DemoActorContainer();
    }

    ActorContainer container;

    Map<String, Long> sessionToPlayerId = new HashMap<>();

    @OnMessage
    public void messageReceived(String msg, Session sess){
        //System.out.println("msg=" + msg + " sess=" + sess + " sessId=" + sess.getId());

        if (container == null){
            System.out.println("Starting server");
            new HeadlessApplication(new SMAFDemo());
            container = factory();
        }
    }

    private void actorContainerThread(){

    }
}
