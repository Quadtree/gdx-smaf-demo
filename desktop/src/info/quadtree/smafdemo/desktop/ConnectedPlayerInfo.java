package info.quadtree.smafdemo.desktop;

import info.quadtree.smafdemo.smaf.Actor;

import javax.websocket.Session;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class ConnectedPlayerInfo {
    private int id;
    private Session webSocketSession;
    private Instant lastMessage;

    // TODO: Remove invalid replication data
    private Map<Actor, Map<String, Object>> lastReplicatedData = new HashMap<>();

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

    public Map<String, Object> considerReplicatingTo(Actor a){
        Map<String, Object> newReplicationData = a.getReplicationData();
        Map<String, Object> ret = new HashMap<>();

        if (!lastReplicatedData.containsKey(a)){
            lastReplicatedData.put(a, new HashMap<>());
        }

        for (String k : newReplicationData.keySet()){
            if (!Objects.equals(newReplicationData.get(k), lastReplicatedData.get(a).get(k))){
                ret.put(k, newReplicationData.get(k));
                lastReplicatedData.get(a).put(k, newReplicationData.get(k));
            }
        }

        return ret;
    }
}
