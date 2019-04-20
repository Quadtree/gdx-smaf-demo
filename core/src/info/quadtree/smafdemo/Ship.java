package info.quadtree.smafdemo;

import com.badlogic.gdx.math.Vector2;
import info.quadtree.smafdemo.smaf.Actor;

public class Ship extends Actor {
    private Vector2 position;

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Ship setPosition(Vector2 position) {
        this.position = position.cpy();
        return this;
    }
}
