package info.quadtree.smafdemo.smaf;

import java.util.HashMap;
import java.util.Map;

public class RPCMessage {
    public RPCMessage() {
    }

    private int targetActor;
    private String rpcMethodName;

    private Object[] params;

    private Boolean isGreeting;

    private int targetPlayerId;

    private String actorType;

    public int getTargetActor() {
        return targetActor;
    }

    public RPCMessage setTargetActor(int targetActor) {
        this.targetActor = targetActor;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public RPCMessage setParams(Object[] params) {
        this.params = params;
        return this;
    }

    public String getRpcMethodName() {
        return rpcMethodName;
    }

    public RPCMessage setRpcMethodName(String rpcMethodName) {
        this.rpcMethodName = rpcMethodName;
        return this;
    }

    public Boolean getGreeting() {
        return isGreeting;
    }

    public RPCMessage setGreeting(Boolean greeting) {
        isGreeting = greeting;
        return this;
    }

    public int getTargetPlayerId() {
        return targetPlayerId;
    }

    public RPCMessage setTargetPlayerId(int targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
        return this;
    }

    public String getActorType() {
        return actorType;
    }

    public RPCMessage setActorType(String actorType) {
        this.actorType = actorType;
        return this;
    }
}
