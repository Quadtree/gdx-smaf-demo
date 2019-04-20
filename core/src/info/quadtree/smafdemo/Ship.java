package info.quadtree.smafdemo;

import com.badlogic.gdx.math.Vector2;
import info.quadtree.smafdemo.smaf.Actor;
import info.quadtree.smafdemo.smaf.ActorContainer;

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

    public void RPC_Server_setThrust(float thrust){
        RPC_Client_setThrust(thrust);


    }

    public void RPC_Server_setTurn(float turn){
        RPC_Client_setTurn(turn);
    }

    public void RPC_Client_setThrust(float thrust){
        this.thrust = thrust;
    }

    public void RPC_Client_setTurn(float turn){
        this.turn = turn;
    }
}
