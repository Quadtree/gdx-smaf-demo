package info.quadtree.smafdemo.smaf;

import java.util.Random;

public abstract class Actor {
    private static Random random = new Random();

    private long id;
    private long owningPlayerId;

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
}
