package info.quadtree.smafdemo;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import info.quadtree.smafdemo.smaf.Actor;
import info.quadtree.smafdemo.smaf.ContainerClient;
import info.quadtree.smafdemo.smaf.SLog;

import java.util.Optional;

public class SMAFDemo extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;

	public static SMAFDemo s;

	private ContainerClient containerClient;

	public SMAFDemo(ContainerClient containerClient) {
		this.containerClient = containerClient;
	}

	@Override
	public void create () {
		SMAFDemo.s = this;
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		containerClient.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		if (containerClient.getContainer() != null) containerClient.getContainer().render();

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private Optional<Ship> getOwnShip(){
		return containerClient.getContainer().getActorsOwnedBy(containerClient.getMyId())
				.filter(it -> it instanceof Ship)
				.map(it -> (Ship)it)
				.findAny();
	}

	@Override
	public boolean keyDown(int keycode) {
		SLog.info(() -> "Key pressed");

		for (Actor a : containerClient.getContainer().getActors()){
			SLog.info(() -> a.getOwningPlayerId() + " =?= " + containerClient.getMyId());
		}

		if (keycode == Input.Keys.W){
			SLog.info(() -> "W pressed");
			getOwnShip().ifPresent(it -> {
				SLog.info(() -> "W RPC sent");
				it.rpc("setThrust", 1);
			});
		}
		if (keycode == Input.Keys.S){
			SLog.info(() -> "S pressed");
			getOwnShip().ifPresent(it -> {
				SLog.info(() -> "S RPC sent");
				it.rpc("setThrust", -1);
			});
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
