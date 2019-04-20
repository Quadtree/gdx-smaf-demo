package info.quadtree.smafdemo;

import com.badlogic.gdx.math.Vector2;
import info.quadtree.smafdemo.smaf.Actor;

public class Ship extends Actor {
    private Vector2 position;

    float thrust;
    float turn;

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Ship setPosition(Vector2 position) {
        this.position = position.cpy();
        return this;
    }

    @Override
    public String getType() {
        return "Ship";
    }

    public void RPC_setThrust(float thrust){
        this.thrust = thrust;
    }

    public void RPC_setTurn(float turn){
        this.turn = turn;
    }
}
