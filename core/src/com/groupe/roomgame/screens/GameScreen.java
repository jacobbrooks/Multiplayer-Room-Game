package com.groupe.roomgame.screens;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.groupe.roomgame.networking.Listener;
import com.groupe.roomgame.networking.Updater;
import com.groupe.roomgame.networking.packets.DataPacket;
import com.groupe.roomgame.objects.Hallway;
import com.groupe.roomgame.objects.Player;
import com.groupe.roomgame.objects.Room;
import com.groupe.roomgame.objects.RoomWalls;
import com.groupe.roomgame.tools.Constants;

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
	private Updater updater;
	
	private ConcurrentHashMap<Integer, Player> gameState;
	private boolean isLeader;

	public GameScreen(SpriteBatch batch, boolean isLeader){
		this.batch = batch;
		this.isLeader = isLeader;
		this.world = new World(new Vector2(0, 0), true);
		this.debug = new Box2DDebugRenderer();
		this.rooms = new Room[6];
		this.gameState = new ConcurrentHashMap<Integer, Player>();
		
		p = new Player(1, gameState, 450f, 450f, world);
		gameState.put(p.getId(), p);
		
		listener = new Listener(gameState, world);
		updater = new Updater();

		if (isLeader) {
			listener.initialListen();
			updater.update(new DataPacket(p.getId(), p.getBody().getPosition().x * 100, p.getBody().getPosition().y * 100));
		} else {
			updater.update(new DataPacket(p.getId(), p.getBody().getPosition().x * 100, p.getBody().getPosition().y * 100));
			listener.initialListen();
		}
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
		
		float lastX = p.getBody().getPosition().x;
		float lastY = p.getBody().getPosition().y;
		
		float lastVelX = p.getBody().getLinearVelocity().x;
		float lastVelY = p.getBody().getLinearVelocity().y;
		
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

		float dx = p.getBody().getPosition().x - lastX;
		float dy = p.getBody().getPosition().y - lastY;
						
		if (dx == 0 && dy == 0)
			System.out.println(dx + " " + dy);
		else
			updater.update(new DataPacket(p.getId(), dx, dy));
		
		Iterator<Integer> it = gameState.keySet().iterator();
		while(it.hasNext()) {
			Player tmp = gameState.get(it.next());
			tmp.render(batch);
		}
		
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