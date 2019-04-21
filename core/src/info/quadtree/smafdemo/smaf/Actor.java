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
        container.sendRPC(id, methodName, args);
    }

    public void executeRPC(RPCMessage rpcMessage, String context) throws ReflectionException {
        for(Method m : ClassReflection.getMethods(this.getClass())){
            if (m.getName().equals("RPC_" + context + "_" + rpcMessage.getRpcMethodName())){
                m.invoke(this, rpcMessage.getParams());
            }
        }
    }

    public Map<String, Object> getReplicationData(){
        final Map<String, Object> ret = new HashMap<>();

        getReplicatedFields().forEach(it -> {
            try {
                Method f = ClassReflection.getMethod(this.getClass(), "get" + it.substring(0, 1).toUpperCase() + it.substring(1));
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
        return Stream.of();
    }
}
