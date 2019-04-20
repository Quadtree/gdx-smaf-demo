package info.quadtree.smafdemo;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import info.quadtree.smafdemo.smaf.Actor;
import info.quadtree.smafdemo.smaf.ActorContainer;

import java.util.Objects;

public class DemoActorContainer extends ActorContainer {
    @Override
    public void playerConnected(long id) {
        Ship ship = new Ship();
        ship.setOwningPlayerId(id);
        ship.setPosition(new Vector2(MathUtils.random(0, 20), MathUtils.random(0, 20)));

        addActor(ship);
    }

    @Override
    public Actor actorFactory(String typeName) {
        if (Objects.equals(typeName, "Ship")) return new Ship();

        return null;
    }
}