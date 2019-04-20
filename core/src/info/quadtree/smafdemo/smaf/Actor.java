package info.quadtree.smafdemo.smaf;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public abstract class Actor {
    private static Random random = new Random();

    private long id;
    private long owningPlayerId;

    private ActorContainer container;

    public Actor(){
        id = random.nextLong();
    }

    void enteringWorld(){}
    void exitingWorld(){}
    void update(){}
    void render(){}
    boolean keep(){ return true; }

    public long getId() {
        return id;
    }

    public Actor setId(long id) {
        this.id = id;
        return this;
    }

    public long getOwningPlayerId() {
        return owningPlayerId;
    }

    public Actor setOwningPlayerId(long owningPlayerId) {
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
                m.invoke(this, rpcMessage.params);
            }
        }
    }
}
