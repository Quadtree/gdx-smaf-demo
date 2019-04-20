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

	public boolean hasHead(){
		return Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop;
	}
	
	@Override
	public void create () {
		if (hasHead()) {
			batch = new SpriteBatch();
			img = new Texture("badlogic.jpg");
		}
	}

	@Override
	public void render () {
		System.out.println("Render called " + Gdx.graphics.getFramesPerSecond());

		if (hasHead()) {
			Gdx.gl.glClearColor(1, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.draw(img, 0, 0);
			batch.end();
		}
	}
	
	@Override
	public void dispose () {
		if (hasHead()) {
			batch.dispose();
			img.dispose();
		}
	}
}
