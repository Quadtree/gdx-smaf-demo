package info.quadtree.smafdemo.smaf;

import java.util.HashMap;
import java.util.Map;

public class RPCMessage {
    long targetActor;
    String rpcMethodName;

    Map<String, Object> params = new HashMap<>();

    public long getTargetActor() {
        return targetActor;
    }

    public RPCMessage setTargetActor(long targetActor) {
        this.targetActor = targetActor;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public RPCMessage setParams(Map<String, Object> params) {
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
}
