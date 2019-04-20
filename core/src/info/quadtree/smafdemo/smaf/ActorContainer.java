package info.quadtree.smafdemo.smaf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ActorContainer {
    private List<Actor> actors = new ArrayList<>();
    private List<Actor> actorAddQueue = new ArrayList<>();
    private Map<Long, Actor> actorMap = new HashMap<>();

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

    public abstract void playerConnected(long id);
    public abstract Actor actorFactory(String typeName);
}
