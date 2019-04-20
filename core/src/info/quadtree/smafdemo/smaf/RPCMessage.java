package info.quadtree.smafdemo.smaf;

import java.util.HashMap;
import java.util.Map;

public class RPCMessage {
    long targetActor;
    String rpcMethodName;

    Object[] params;

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
}
