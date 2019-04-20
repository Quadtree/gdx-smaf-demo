package info.quadtree.smafdemo.smaf;

import java.util.HashMap;
import java.util.Map;

public class RPCMessage {
    public RPCMessage() {
    }

    private long targetActor;
    private String rpcMethodName;

    private Object[] params;

    private Boolean isGreeting;


    public long getTargetActor() {
        return targetActor;
    }

    public RPCMessage setTargetActor(long targetActor) {
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
}
