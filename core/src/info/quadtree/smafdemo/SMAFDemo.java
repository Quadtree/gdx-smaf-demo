package info.quadtree.smafdemo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SMAFDemo extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	DemoActorContainer container;

	public static SMAFDemo s;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		container = new DemoActorContainer();
	}

	@Override
	public void render () {
		System.out.println("Render called " + Gdx.graphics.getFramesPerSecond());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		container.render();

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
