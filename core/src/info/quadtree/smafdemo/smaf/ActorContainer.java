package info.quadtree.smafdemo.smaf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class ActorContainer {
    private List<Actor> actors = new ArrayList<>();
    private List<Actor> actorAddQueue = new ArrayList<>();
    private Map<Integer, Actor> actorMap = new HashMap<>();

    private Consumer<RPCMessage> rpcMessageSender = (msg) -> { System.err.println("Warning: No-op sender has been used."); };

    public void update(){
        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep()){
                actors.get(i).update();

                for (Actor toAdd : actorAddQueue){
                    toAdd.enteringWorld();
                    actors.add(toAdd);
                    actorMap.put(toAdd.getId(), toAdd);
                }
                actorAddQueue.clear();
            } else {
                actorMap.remove(actors.get(i).getId());
                actors.remove(i--);
            }
        }
    }

    public void render(){
        for (Actor a : actors) a.render();
    }

    public void addActor(Actor actor){
        actorAddQueue.add(actor);
    }

    public Actor getActorById(int id){
        return actorMap.get(id);
    }

    public Stream<Actor> getActorsOwnedBy(int playerId){
        return actors.stream().filter(it -> it.getOwningPlayerId() == playerId);
    }

    public void sendRPC(int targetActor, String methodName, Object... args){
        RPCMessage rpcMessage = new RPCMessage();
        rpcMessage.setTargetActor(targetActor);
        rpcMessage.setRpcMethodName(methodName);
        rpcMessage.setParams(args);
        rpcMessage.setActorType(getActorById(targetActor).getClass().getCanonicalName());

        rpcMessageSender.accept(rpcMessage);
    }

    public Consumer<RPCMessage> getRpcMessageSender() {
        return rpcMessageSender;
    }

    public ActorContainer setRpcMessageSender(Consumer<RPCMessage> rpcMessageSender) {
        this.rpcMessageSender = rpcMessageSender;
        return this;
    }

    public abstract void playerConnected(int id);

    public Actor createBlankActor(String typeName){
        try {
            Actor a = (Actor)ClassReflection.forName(typeName).newInstance();
            actors.add(a);
            return a;
        } catch (ReflectionException|InstantiationException|IllegalAccessException ex){
            Gdx.app.log("SMAF", "Can't create " + typeName + ": " + ex);
        }

        return null;
    }
}
