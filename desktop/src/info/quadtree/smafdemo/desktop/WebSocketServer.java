package info.quadtree.smafdemo.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import info.quadtree.smafdemo.DemoActorContainer;
import info.quadtree.smafdemo.SMAFDemo;
import info.quadtree.smafdemo.smaf.Actor;
import info.quadtree.smafdemo.smaf.ActorContainer;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import info.quadtree.smafdemo.smaf.RPCMessage;

@ServerEndpoint("/smafserver")
public class WebSocketServer {
    private ActorContainer factory(){
        return new DemoActorContainer();
    }

    Logger log = Logger.getLogger(WebSocketServer.class.getName());

    ActorContainer container;

    Map<String, ConnectedPlayerInfo> sessionMap = new HashMap<>();

    Random rand = new Random();

    private long updateTimeDone;

    @OnMessage
    public void messageReceived(String msg, Session sess){
        if (container == null){
            System.out.println("Starting server");
            new HeadlessApplication(new NoOpAppListener());
            container = factory();

            updateTimeDone = System.currentTimeMillis();

            Thread updateThread = new Thread(this::updateThread);
            updateThread.setDaemon(true);
            updateThread.start();
        }

        try {
            synchronized (container) {
                if (!sessionMap.containsKey(sess.getId())) {
                    int newPlayerId = rand.nextInt();
                    log.info("New player connected, assigned ID " + newPlayerId);

                    ConnectedPlayerInfo cpi = new ConnectedPlayerInfo();
                    cpi.setId(newPlayerId);
                    cpi.setWebSocketSession(sess);

                    sessionMap.put(sess.getId(), cpi);
                }

                sessionMap.get(sess.getId()).setLastMessage(Instant.now());

                // this should be a RPC, that's all clients can send
                Json js = new Json();
                RPCMessage rpcMessage = js.fromJson(RPCMessage.class, msg);
                if (rpcMessage != null) {

                    Actor actor = container.getActorById(rpcMessage.getTargetActor());
                    if (actor != null) {
                        if (actor.getOwningPlayerId() == sessionMap.get(sess.getId()).getId()) {
                            actor.executeRPC(rpcMessage, "Server");
                        } else {
                            log.warning("Player attempted to modify actor " + actor.getId() + " but was not the owner");
                        }
                    } else {
                        log.warning("RPC received for non-existant actor " + rpcMessage.getTargetActor());
                    }
                } else {
                    log.warning("Message sent from client was not a valid RPC");
                }
            }
        } catch (Throwable err){
            log.warning("Error processing message: " + err);
        }
    }

    private void updateThread(){
        while(true){
            if (System.currentTimeMillis() > updateTimeDone){
                synchronized (container) {
                    container.update();
                }
                updateTimeDone += 16;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    private static class NoOpAppListener implements ApplicationListener {
        @Override
        public void create() {}

        @Override
        public void resize(int width, int height) {}

        @Override
        public void render() {}

        @Override
        public void pause() {}

        @Override
        public void resume() {}

        @Override
        public void dispose() {}
    }
}
