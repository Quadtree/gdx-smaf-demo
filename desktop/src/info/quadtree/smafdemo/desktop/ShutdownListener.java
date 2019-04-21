package info.quadtree.smafdemo.desktop;

import info.quadtree.smafdemo.smaf.SLog;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ShutdownListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SLog.info(() -> "Shutdown requested...");
        WebSocketServer.keepRunning = false;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
