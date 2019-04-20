package info.quadtree.smafdemo.smaf;

import java.util.ArrayList;
import java.util.List;

public class ActorContainer {
    private List<Actor> actors = new ArrayList<>();
    private List<Actor> actorAddQueue = new ArrayList<>();

    public void update(){
        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep()){
                actors.get(i).update();

                for (Actor toAdd : actorAddQueue){
                    toAdd.enteringWorld();
                    actors.add(toAdd);
                }
                actorAddQueue.clear();
            } else {
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
}
