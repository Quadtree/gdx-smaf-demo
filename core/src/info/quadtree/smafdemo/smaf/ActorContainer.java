package info.quadtree.smafdemo.smaf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ActorContainer {
    private List<Actor> actors = new ArrayList<>();
    private List<Actor> actorAddQueue = new ArrayList<>();
    private Map<Long, Actor> actorMap = new HashMap<>();

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

    public Actor getActorById(long id){
        return actorMap.get(id);
    }

    public void sendRPC(long targetActor, String methodName, Object[]... args){
        RPCMessage rpcMessage = new RPCMessage();
        rpcMessage.setTargetActor(targetActor);
        rpcMessage.setRpcMethodName(methodName);
        rpcMessage.setParams(args);

        rpcMessageSender.accept(rpcMessage);
    }

    public abstract void playerConnected(long id);
}
