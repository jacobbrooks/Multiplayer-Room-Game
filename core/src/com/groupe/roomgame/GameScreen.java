package com.groupe.roomgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen{

	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	private SpriteBatch batch;

	public GameScreen(SpriteBatch batch){
		this.batch = batch;
	}

	private void loadMap(String mapName) {
		map = new TmxMapLoader().load(mapName);
		renderer = new OrthogonalTiledMapRenderer(map, 1);
	}
	
	private void loadCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
}

	@Override
	public void show() {
		loadCamera();
		loadMap("map/map.tmx");
	}


	@Override
	public void render(float delta) {
		renderer.render();
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			camera.position.x++;
			camera.update();
		}
		renderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void resize(int width, int height) {

	}


	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}