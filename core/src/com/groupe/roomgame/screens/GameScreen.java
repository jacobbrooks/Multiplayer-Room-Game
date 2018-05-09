package com.groupe.roomgame.screens;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.groupe.roomgame.networking.Listener;
import com.groupe.roomgame.networking.packets.DataPacket;
import com.groupe.roomgame.networking.Updater;
import com.groupe.roomgame.objects.Animal;
import com.groupe.roomgame.objects.Character;
import com.groupe.roomgame.objects.Hallway;
import com.groupe.roomgame.objects.NPC;
import com.groupe.roomgame.objects.Player;
import com.groupe.roomgame.objects.Room;
import com.groupe.roomgame.objects.RoomWalls;
import com.groupe.roomgame.tools.BodyBuilder;
import com.groupe.roomgame.tools.Constants;
import com.groupe.roomgame.networking.Heartbeat;
import com.groupe.roomgame.networking.election.IPs;
import com.groupe.roomgame.objects.Character;
import com.groupe.roomgame.objects.Animal;

public class GameScreen implements Screen{

	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private Box2DDebugRenderer debug;
	private TiledMap map;
	private SpriteBatch batch;
	private World world;
	private Room[] rooms;
	private Character pc;
	private Listener listener;
	private Updater updater;

	private ConcurrentHashMap<Integer, Character> gameState;
	private boolean isLeader;
	
	private Random r;

	public GameScreen(SpriteBatch batch, boolean isLeader){
		this.batch = batch;
		this.isLeader = isLeader;
		this.world = new World(new Vector2(0, 0), true);
		this.debug = new Box2DDebugRenderer();
		this.rooms = new Room[6];

		/*Thread t = new Thread(new Heartbeat(IPs.getIPsAsList,isLeader));
		t.start();*/
		
		this.gameState = new ConcurrentHashMap<Integer, Character>();
		
		r = new Random();

		float[] pcCoordinates = randomRoomCoordinates();
		pc = new Player(r.nextInt(10000), pcCoordinates[0], pcCoordinates[1], world);
		gameState.put(pc.getId(), pc);
		
		loadCamera();
		loadMap("map/map.tmx");
		loadObjects();

		listener = new Listener(gameState, rooms, world, isLeader);
		updater = new Updater();

		DataPacket myPlayerInitPacket = new DataPacket();
		
		if (isLeader) {
			System.out.println("I am leader in here");
			
			listener.initialListen();
			
			myPlayerInitPacket.createCharacterInitPacket(Character.PC, pc.getId(), pc.getBody().getPosition().x * 100, pc.getBody().getPosition().y * 100);
			updater.update(myPlayerInitPacket, isLeader);
			
			Character[] generatedCharacters = generateCharacters();
			for(Character c : generatedCharacters) {
				gameState.put(c.getId(), c);
				DataPacket characterInitPacket = new DataPacket();
				characterInitPacket.createCharacterInitPacket(c.getType(), c.getId(), c.getBody().getPosition().x * 100, c.getBody().getPosition().y * 100);
				updater.update(characterInitPacket, isLeader);
			}
			
			for(Room room : rooms) {
				room.setRoomState(r.nextInt(3));
				DataPacket roomUpdatePacket = new DataPacket();
				roomUpdatePacket.createRoomUpdatePacket(room.getID(), room.getRoomState());
				updater.update(roomUpdatePacket, isLeader);
			}
					
		} else {
			System.out.println("I am not the leader in here");
			myPlayerInitPacket.createCharacterInitPacket(Character.PC, pc.getId(), pc.getBody().getPosition().x * 100, pc.getBody().getPosition().y * 100);
			updater.update(myPlayerInitPacket, isLeader);
			listener.initialListen();
		}
		
		listener.updateListen();
	}
	
	private Character[] generateCharacters() {
		Character[] characters = new Character[20];
		for(int i = 0; i < 20; i++) {
			boolean animal = r.nextBoolean();
			
			float[] roomCoordinates = randomRoomCoordinates();
			
			int ranID = r.nextInt(10000);
			
			Character c;
			if(animal) {
				c = new Animal(ranID, roomCoordinates[0], roomCoordinates[1], world);
			}else {
				c = new NPC(ranID, roomCoordinates[0], roomCoordinates[1], world);
			}
			characters[i] = c;
		}	
		return characters;
	}
	
	private float[] randomRoomCoordinates() {
		int randomRoom = r.nextInt(6);
		
		float roomPosX = rooms[randomRoom].getRect().x;
		float roomPosY = rooms[randomRoom].getRect().y;
		float roomWidth = rooms[randomRoom].getRect().width;
		float roomHeight = rooms[randomRoom].getRect().height;
		
		float ranX = r.nextInt((int) roomWidth) + roomPosX - 64;
		float ranY = r.nextInt((int) roomHeight) + roomPosY - 64;
		
		return new float[]{ranX, ranY};
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
		loadRooms();
	}

	private void loadRooms() {
		for (int i = 0; i < rooms.length; i++) {
			for (MapObject object : map.getLayers().get("Room " + (i + 1)).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				Body body = BodyBuilder.createBox(world, Constants.STATIC_BODY, rect, Constants.INTERACTIVE_BITS, Constants.INTERACTIVE_BITS, this);
				rooms[i] = new Room(i + 1, rect, body);
			}
		}
	}

	@Override
	public void show() {
	
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//System.out.println(delta);
		pc.update(delta);
		pc.getRoom(rooms);
		renderer.render();
		renderer.setView(camera);

		camera.position.set(pc.getBody().getPosition().x, pc.getBody().getPosition().y, 0);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		float lastX = pc.getBody().getPosition().x;
		float lastY = pc.getBody().getPosition().y;

		float lastVelX = pc.getBody().getLinearVelocity().x;
		float lastVelY = pc.getBody().getLinearVelocity().y;

		pc.getBody().setLinearVelocity(new Vector2(0f, 0f));
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			pc.getBody().setLinearVelocity(new Vector2(-1f, 0f));
			pc.getSprite().setRotation((float) Math.toDegrees(Math.PI));
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			pc.getBody().setLinearVelocity(new Vector2(1f, 0f));
			pc.getSprite().setRotation((float) 0f);
		}
		if(Gdx.input.isKeyPressed(Keys.UP)){
			pc.getBody().setLinearVelocity(new Vector2(0f, 1f));
			pc.getSprite().setRotation((float) Math.toDegrees(Math.PI / 2));
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			pc.getBody().setLinearVelocity(new Vector2(0f, -1f));
			pc.getSprite().setRotation((float) Math.toDegrees(3 * Math.PI / 2));
		}

		world.step(1/60f, 8, 3);

		float dx = pc.getBody().getPosition().x - lastX;
		float dy = pc.getBody().getPosition().y - lastY;

		if (dx != 0 || dy != 0){
			DataPacket packet = new DataPacket();
			packet.createCharacterUpdatePacket(pc.getId(), pc.getRespect(), dx, dy);
			updater.update(packet, isLeader);
		}
			

		Iterator<Integer> it = gameState.keySet().iterator();
		while(it.hasNext()) {
			Character tmp = gameState.get(it.next());
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
