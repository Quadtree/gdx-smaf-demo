package info.quadtree.smafdemo.desktop;

import com.badlogic.gdx.ApplicationListener;
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

    Map<String, ConnectedPlayerInfo> sessionMap = new HashMap<>();

    long updateTimeDone;

    @OnMessage
    public void messageReceived(String msg, Session sess){
        //System.out.println("msg=" + msg + " sess=" + sess + " sessId=" + sess.getId());

        if (container == null){
            System.out.println("Starting server");
            new HeadlessApplication(new NoOpAppListener());
            container = factory();

            updateTimeDone = System.currentTimeMillis();

            Thread updateThread = new Thread(this::updateThread);
            updateThread.setDaemon(true);
            updateThread.start();
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
