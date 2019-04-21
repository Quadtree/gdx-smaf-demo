package info.quadtree.smafdemo.smaf;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

public abstract class Actor {
    private static Random random = new Random();

    private int id;
    private int owningPlayerId;

    private ActorContainer container;

    public Actor(){
        id = random.nextInt();
    }

    public void enteringWorld(){}
    public void exitingWorld(){}
    public void update(){}
    public void render(){}
    public boolean keep(){ return true; }

    public int getId() {
        return id;
    }

    public Actor setId(int id) {
        this.id = id;
        return this;
    }

    public int getOwningPlayerId() {
        return owningPlayerId;
    }

    public Actor setOwningPlayerId(int owningPlayerId) {
        this.owningPlayerId = owningPlayerId;
        return this;
    }

    public ActorContainer getContainer() {
        return container;
    }

    public Actor setContainer(ActorContainer container) {
        this.container = container;
        return this;
    }

    public void rpc(String methodName, Object... args){
        if (container == null) throw new RuntimeException("container cannot be null if trying to send RPC");
        container.sendRPC(id, methodName, args);
    }

    public void executeRPC(RPCMessage rpcMessage, String context) throws ReflectionException {
        String methodName = "RPC_" + context + "_" + rpcMessage.getRpcMethodName();

        for(Method m : ClassReflection.getMethods(this.getClass())){
            if (m.getName().equals(methodName)){
                m.invoke(this, rpcMessage.getParams());
                return;
            }
        }

        SLog.warn(() -> "Cannot find RPC target " + methodName);
    }

    public Map<String, Object> getReplicationData(){
        final Map<String, Object> ret = new HashMap<>();

        getReplicatedFields().forEach(it -> {
            try {
                Method f = MethodMapper.getGetterMethod(getClass(), it);
                Object o = f.invoke(this);
                if (o != null){
                    ret.put(it, o);
                }
            } catch (ReflectionException ex){
                throw new RuntimeException(ex);
            }
        });

        return ret;
    }

    public Stream<String> getReplicatedFields(){
        return Stream.of("owningPlayerId");
    }

    public void RPC_Client_replicate(Map<String, Object> replicationData){
        for (Map.Entry<String, Object> entry : replicationData.entrySet()){
            try {
                Method setter = MethodMapper.getSetterMethod(getClass(), entry.getKey());
                if (setter.getParameterTypes()[0] == float.class) setter.invoke(this, (float)entry.getValue());
                if (setter.getParameterTypes()[0] == double.class) setter.invoke(this, (double)entry.getValue());
                else if (setter.getParameterTypes()[0] == int.class) setter.invoke(this, (int)entry.getValue());
                else setter.invoke(this, entry.getValue());

            } catch (ReflectionException ex){
                throw new RuntimeException(ex);
            }
        }
    }
}
