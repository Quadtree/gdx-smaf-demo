package info.quadtree.smafdemo.smaf;

import java.util.Random;

public class Actor {
    private static Random random = new Random();

    private long id;

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
}
