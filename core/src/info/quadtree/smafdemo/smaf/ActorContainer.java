package info.quadtree.smafdemo.smaf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ActorContainer implements Disposable {
    private List<Actor> actors = new ArrayList<>();
    private List<Actor> actorAddQueue = new ArrayList<>();
    private Map<Integer, Actor> actorMap = new HashMap<>();

    private Consumer<RPCMessage> rpcMessageSender = (msg) -> { SLog.error(() -> "Warning: No-op sender has been used."); };

    public void update(){
        flushAddQueue();

        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep()){
                actors.get(i).update();

                flushAddQueue();
            } else {
                actorMap.remove(actors.get(i).getId());
                actors.remove(i--);
            }
        }
    }

    private void flushAddQueue() {
        for (Actor toAdd : actorAddQueue){
            toAdd.enteringWorld();
            actors.add(toAdd);
            actorMap.put(toAdd.getId(), toAdd);
        }
        actorAddQueue.clear();
    }

    public void render(){
        for (Actor a : actors) a.render();
    }

    public void addActor(Actor actor){
        actor.setContainer(this);
        actorAddQueue.add(actor);
    }

    public Actor getActorById(int id){
        return actorMap.get(id);
    }

    public Stream<Actor> getActorsOwnedBy(int playerId){
        return actors.stream().filter(it -> it.getOwningPlayerId() == playerId);
    }

    public void sendRPC(Actor targetActor, String methodName, Object... args){
        if (targetActor == null) throw new RuntimeException("Cannot find actor with ID " + targetActor);

        RPCMessage rpcMessage = new RPCMessage();
        rpcMessage.setTargetActor(targetActor.getId());
        rpcMessage.setRpcMethodName(methodName);
        rpcMessage.setParams(args);
        rpcMessage.setActorType(targetActor.getClass().getCanonicalName());

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
    public abstract void playerDisconnected(int id);

    public Actor createBlankActor(String typeName, int id){
        try {
            Actor a = (Actor)ClassReflection.newInstance(ClassReflection.forName(typeName));
            a.setContainer(this);
            a.setId(id);
            actors.add(a);
            actorMap.put(id, a);
            return a;
        } catch (ReflectionException ex){
            SLog.warn(() -> "Can't create " + typeName + ": " + ex);
        }

        return null;
    }

    public List<Actor> getActors() {
        return Collections.unmodifiableList(actors);
    }

    @Override
    public void dispose() {
        // currently no-op
    }
}
