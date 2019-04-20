package info.quadtree.smafdemo.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import info.quadtree.smafdemo.DemoActorContainer;
import info.quadtree.smafdemo.SMAFDemo;
import info.quadtree.smafdemo.smaf.*;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import com.badlogic.gdx.backends.headless.HeadlessApplication;

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
        if (SLog.logCallback == null) {
            SLog.logCallback = (level, logMessage) -> {
                switch(level){
                    case Debug: log.fine(logMessage); break;
                    case Info: log.info(logMessage); break;
                    case Warn: log.warning(logMessage); break;
                    case Error: log.severe(logMessage); break;
                }
            };
        }

        if (container == null){
            System.out.println("Starting server");
            new HeadlessApplication(new NoOpAppListener());
            container = factory();
            container.setRpcMessageSender(this::send);

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

                    RPCMessage greetingMessage = new RPCMessage();
                    greetingMessage.setGreeting(true);
                    greetingMessage.setTargetPlayerId(newPlayerId);

                    Json js = new Json();
                    sess.getBasicRemote().sendText(js.toJson(greetingMessage));
                }

                sessionMap.get(sess.getId()).setLastMessage(Instant.now());

                // this should be a RPC, that's all clients can send
                Json js = new Json();
                RPCMessage rpcMessage = js.fromJson(RPCMessage.class, msg);
                if (rpcMessage != null) {
                    if (!rpcMessage.getGreeting()) {
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
                    }
                } else {
                    log.warning("Message sent from client was not a valid RPC");
                }
            }
        } catch (Throwable err){
            log.warning("Error processing message: " + err);
        }
    }

    public void send(RPCMessage message){
        Json js = new Json();
        String text = js.toJson(message);

        for (ConnectedPlayerInfo connectedPlayerInfo : sessionMap.values()){
            try {
                connectedPlayerInfo.getWebSocketSession().getBasicRemote().sendText(text);
            } catch (Throwable err){
                log.warning("Error broadcasting message: " + err);
            }
        }
    }

    private void updateThread(){
        while(true){
            if (System.currentTimeMillis() > updateTimeDone){
                synchronized (container) {
                    container.update();

                    for (Actor a : container.getActors()){
                        for (ConnectedPlayerInfo cpi : sessionMap.values()){
                            Map<String, Object> replicationData = cpi.considerReplicatingTo(a);
                            if (replicationData != null){
                                a.rpc("replicate", replicationData);
                            }
                        }
                    }
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
