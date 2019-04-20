package info.quadtree.smafdemo.desktop;

import javax.websocket.Session;
import java.time.Instant;

public class ConnectedPlayerInfo {
    private int id;
    private Session webSocketSession;
    private Instant lastMessage;

    public int getId() {
        return id;
    }

    public ConnectedPlayerInfo setId(int id) {
        this.id = id;
        return this;
    }

    public Session getWebSocketSession() {
        return webSocketSession;
    }

    public ConnectedPlayerInfo setWebSocketSession(Session webSocketSession) {
        this.webSocketSession = webSocketSession;
        return this;
    }

    public Instant getLastMessage() {
        return lastMessage;
    }

    public ConnectedPlayerInfo setLastMessage(Instant lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }
}
