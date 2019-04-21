package info.quadtree.smafdemo;

import com.badlogic.gdx.math.Vector2;
import info.quadtree.smafdemo.smaf.Actor;
import info.quadtree.smafdemo.smaf.ActorContainer;

import java.util.List;
import java.util.stream.Stream;

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

        rpc("setThrust", thrust);
    }

    public void RPC_Server_setTurn(float turn){
        RPC_Client_setTurn(turn);

        rpc("setTurn", thrust);
    }

    public void RPC_Client_setThrust(float thrust){
        this.thrust = thrust;
    }

    public void RPC_Client_setTurn(float turn){
        this.turn = turn;
    }

    @Override
    public void update() {
        super.update();

        this.position.y += thrust;
    }

    @Override
    public void render() {
        super.render();

        SMAFDemo.s.batch.draw(SMAFDemo.s.img, position.x, position.y, 32, 32);
    }

    @Override
    public Stream<String> getReplicatedFields() {
        return Stream.concat(super.getReplicatedFields(), Stream.of("thrust", "turn", "position"));
    }

    public float getThrust() {
        return thrust;
    }

    public Ship setThrust(float thrust) {
        this.thrust = thrust;
        return this;
    }

    public float getTurn() {
        return turn;
    }

    public Ship setTurn(float turn) {
        this.turn = turn;
        return this;
    }
}
