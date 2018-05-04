package com.groupe.roomgame.screens;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.groupe.roomgame.objects.Hallway;
import com.groupe.roomgame.objects.Player;
import com.groupe.roomgame.objects.Room;
import com.groupe.roomgame.objects.RoomWalls;
import com.groupe.roomgame.tools.Constants;
import com.groupe.roomgame.networking.Listener;
import com.badlogic.gdx.Input.Keys;

public class GameScreen implements Screen{

	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private Box2DDebugRenderer debug;
	private TiledMap map;
	private SpriteBatch batch;
	private World world;
	private Room[] rooms;
	private Player p;
	private Listener listener;
	
	private ConcurrentHashMap<Integer, Player> gameState;


	public GameScreen(SpriteBatch batch){
		this.batch = batch;
		this.world = new World(new Vector2(0, 0), true);
		this.debug = new Box2DDebugRenderer();
		this.rooms = new Room[6];
		this.gameState = new ConcurrentHashMap<Integer, Player>();
		p = new Player(0, gameState, 350f, 350f, world);
		gameState.put(p.getId(), p);
		listener = new Listener(gameState, world);
		listener.initialListen();
		listener.updateListen();
	}

	private void loadMap(String mapName) {
		map = new TmxMapLoader().load(mapName);
		renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.SCALE);
	}
	
	private void loadCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / Constants.SCALE, Gdx.graphics.getHeight() / Constants.SCALE);
		camera.update();
	}
	
	private void loadObjects(){
		new Hallway(map, world, "Hallway");
		new RoomWalls(map, world, "Room Walls");
		for (int i = 0; i < rooms.length; i++)
			rooms[i] = new Room(map, world, "Room " + (i + 1));
	}

	@Override
	public void show() {
		loadCamera();
		loadMap("map/map.tmx");
		loadObjects();
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		renderer.setView(camera);
		
		camera.position.set(p.getSprite().getX(), p.getSprite().getY(), 0);
		camera.update();
		
		
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		p.getBody().setLinearVelocity(new Vector2(0f, 0f));
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			p.getBody().setLinearVelocity(new Vector2(-1f, 0f));
			p.getSprite().setRotation((float) Math.toDegrees(Math.PI));
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			p.getBody().setLinearVelocity(new Vector2(1f, 0f));
			p.getSprite().setRotation((float) 0f);
		}
		if(Gdx.input.isKeyPressed(Keys.UP)){
			p.getBody().setLinearVelocity(new Vector2(0f, 1f));
			p.getSprite().setRotation((float) Math.toDegrees(Math.PI / 2));
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			p.getBody().setLinearVelocity(new Vector2(0f, -1f));
			p.getSprite().setRotation((float) Math.toDegrees(3 * Math.PI / 2));
		}

		world.step(1/60f, 8, 3);
		
		Iterator<Integer> it = gameState.keySet().iterator();
		while(it.hasNext()) {
			Player tmp = gameState.get(it.next());
			tmp.render(batch);
		}
		
		//p.render(batch);
		batch.end();
		
		debug.render(world, camera.combined);
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