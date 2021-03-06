package com.groupe.roomgame.screens;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.IOException;

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
import com.badlogic.gdx.graphics.Texture;
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
import com.groupe.roomgame.networking.election.HoldElection;
import com.groupe.roomgame.objects.Character;
import com.groupe.roomgame.objects.Animal;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
	public static boolean leaderIsDead;
	public static int leaderId;

	private ConcurrentHashMap<Integer, Character> gameState;
	private boolean isLeader;

	private Random rand;

	public GameScreen(SpriteBatch batch, boolean isLeader){
		this.batch = batch;
		this.isLeader = isLeader;
		this.world = new World(new Vector2(0, 0), true);
		this.debug = new Box2DDebugRenderer();
		this.rooms = new Room[6];
		this.leaderIsDead = false;
		this.rand = new Random();

		Thread t = new Thread(new Heartbeat(IPs.getIPsAsList,isLeader));
		t.start();

		this.gameState = new ConcurrentHashMap<Integer, Character>();

		loadCamera();
		loadMap("map/map.tmx");
		loadObjects();

		float[] pcCoordinates = randomRoomCoordinates(true, 0);
		pc = new Player(rand.nextInt(10000), pcCoordinates[0], pcCoordinates[1], world);
		gameState.put(pc.getId(), pc);

		listener = new Listener(gameState, rooms, world, isLeader);
		updater = new Updater();

		DataPacket initPacket = new DataPacket();

		if (isLeader) {
			System.out.println("I am leader in here");

			for (int i = 0; i < IPs.getIPs.length - 1; i++)
				listener.initialListen();

			Character[] generatedCharacters = generateCharacters();
			initPacket.createInitPacket(rooms, gameState);

			updater.update(initPacket, isLeader);
		} else {
			System.out.println("I am not the leader in here");

			initPacket.createCharacterInitPacket(pc);
			updater.update(initPacket, isLeader);

			while (!listener.initialListen()){
				updater.update(initPacket, isLeader);
			}
		}

		listener.updateListen();
	}

	private Character[] generateCharacters() {
		Character[] characters = new Character[15];
		for(int i = 0; i < 15; i++) {
			boolean animal = rand.nextBoolean();

			float[] roomCoordinates = randomRoomCoordinates(true, 0);

			int ranID = rand.nextInt(10000);

			Character c;
			if (animal) {
				c = new Animal(ranID, roomCoordinates[0], roomCoordinates[1], world);
			}else {
				c = new NPC(ranID, roomCoordinates[0], roomCoordinates[1], world);
			}
			characters[i] = c;
			gameState.put(ranID, c);
		}
		return characters;
	}

	public float[] randomRoomCoordinates(boolean random, int roomID) {

		int roomIndex = -1;

		if(random){
			roomIndex = rand.nextInt(6);
		}else{
			roomIndex = roomID;
		}

		Vector2 center = new Vector2();
		rooms[roomIndex].getRect().getCenter(center);
		int halfWidth = (int) rooms[roomIndex].getRect().getWidth() / 2;
		int halfHeight = (int) rooms[roomIndex].getRect().getHeight() / 2;

		boolean subtractX = rand.nextBoolean();
		boolean subtractY = rand.nextBoolean();

		int xOffset = 0;
		int yOffset = 0;

		float roomPosX = center.x;
		float roomPosY = center.y;

		if(subtractX){
			xOffset = rand.nextInt(halfWidth - 25);
			roomPosX -= xOffset;
		}else{
			xOffset = rand.nextInt(halfWidth - (64 + 25));
			roomPosX += xOffset;
		}

		if(subtractY){
			yOffset = rand.nextInt(halfHeight - 25);
			roomPosY -= yOffset;
		}else{
			xOffset = rand.nextInt(halfHeight - (64 + 25));
			roomPosY += yOffset;
		}

		return new float[]{roomPosX, roomPosY};
	}

	/*private void printLabel(SpriteBatch batch){
		BitmapFont font;
		String s = "Respect: " + pc.getRespect();
		CharSequence str = s;
		font = new BitmapFont();
		font.setScale(2f);
		font.draw(batch, str, pc.getX() * 100, (pc.getY() * 100) + 30);
	}*/

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

		for(Room r : rooms){
			r.generateSocks();
			r.generateLemonPledge();
		}
	}

	private void loadRooms() {
		for (int i = 0; i < rooms.length; i++) {
			for (MapObject object : map.getLayers().get("Room " + i).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				Body body = BodyBuilder.createBox(world, Constants.STATIC_BODY, rect, Constants.INTERACTIVE_BITS, Constants.INTERACTIVE_BITS, this);
				rooms[i] = new Room(i, rect, body, this);
				rooms[i].setRoomState(rand.nextInt(3));
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

		world.step(1/60f, 8, 3);

		if (leaderIsDead){
			reholdElection();
		}

		renderer.render();
		renderer.setView(camera);
		camera.position.set(pc.getBody().getPosition().x, pc.getBody().getPosition().y, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		handleInput();
		pc.update(pc.getBody().getPosition().x, pc.getBody().getPosition().y, pc.getRespect());
		pc.setRoom(rooms);

		if(pc.getRoom().getRoomState() == Room.DIRTY){
			pc.getRoom().renderDirtyRoom(batch);
		} else if(pc.getRoom().getRoomState() == Room.CLEAN){
			pc.getRoom().renderCleanRoom(batch);
		}

		System.out.println("Room: " + pc.getRoom().getID() + " - state: " + pc.getRoom().getRoomState());

		sendCharacterUpdatePacket(pc);
		updateState();

		//printLabel(batch);

		batch.end();
		//debug.render(world, camera.combined);
	}

	private void sendRoomUpdatePacket(Room room){
		DataPacket packet = new DataPacket();
		packet.createRoomUpdatePacket(room.getID(), room.getRoomState());
		updater.update(packet, isLeader);
	}

	private void sendCharacterUpdatePacket(Character c){
		DataPacket packet = new DataPacket();
		packet.createCharacterUpdatePacket(c.getId(), c.getRespect(), c.getBody().getPosition().x, c.getBody().getPosition().y);
		updater.update(packet, isLeader);
	}

	public void updateState(){
		Iterator<Integer> it = gameState.keySet().iterator();
		while(it.hasNext()) {
			Character tmp = gameState.get(it.next());
			tmp.render(batch);
			if (!world.isLocked()){
				if (tmp.getId() != pc.getId())
					tmp.getBody().setTransform(tmp.getX(), tmp.getY(), 0);
			}
		}
	}

	private void handleInput(){
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

		if (Gdx.input.isKeyJustPressed(Keys.D)){
			pc.dirtyRoom();
			changeRespect(true);
			check();
			sendRoomUpdatePacket(pc.getRoom());
		}

		if (Gdx.input.isKeyJustPressed(Keys.C)){
			pc.cleanRoom();
			changeRespect(false);
			check();
			sendRoomUpdatePacket(pc.getRoom());
		}
	}

	private void changeRespect(boolean dirty){
		Iterator<Integer> it = gameState.keySet().iterator();
		while(it.hasNext()) {
			Character tmp = gameState.get(it.next());
			if (tmp instanceof Player)
				continue;
			tmp.setRoom(rooms);
			pc.setRoom(rooms);

			if (pc.getRoom() == null)
				System.out.println("PC ROOM NULL");
			if (tmp.getRoom() == null)
				System.out.println("ANIMAL ROOM NULL");

			if (pc.getRoom().getID() != tmp.getRoom().getID())
				continue;
			if (tmp instanceof Animal && dirty)
				pc.setRespect(pc.getRespect() - 1);
			else if (tmp instanceof Animal && !dirty)
				pc.setRespect(pc.getRespect() + 1);
			if (tmp instanceof NPC && dirty)
				pc.setRespect(pc.getRespect() + 1);
			else if (tmp instanceof NPC && !dirty)
				pc.setRespect(pc.getRespect() - 1);
		}
	}

	private void check(){
		Iterator<Integer> it = gameState.keySet().iterator();
		while(it.hasNext()) {
			Character tmp = gameState.get(it.next());
			tmp.setRoom(rooms);
			pc.setRoom(rooms);
			if (!(tmp instanceof Player) && pc.getRoom().getID() == tmp.getRoom().getID()){
				if (tmp.check(rooms, this)){
					updateState();
					sendRoomUpdatePacket(tmp.getRoom());
					sendCharacterUpdatePacket(tmp);
				}
			}
		}
	}

	private void reholdElection(){
		try {
			IPs.getIPsAsList.remove(IPs.leader);

			String[] tmp = new String[IPs.getIPsAsList.size()];
			for (int i = 0; i < tmp.length; i++)
				tmp[i] = IPs.getIPsAsList.get(i).getHostAddress();

			IPs.needToVote = new CopyOnWriteArrayList<String>(tmp);
			IPs.needToVoteStored = new CopyOnWriteArrayList<String>(IPs.needToVote);
			HoldElection election = new HoldElection(2703);
			isLeader = election.run();
			listener.setLeader(isLeader);
			leaderIsDead = false;
			Thread t = new Thread(new Heartbeat(IPs.getIPsAsList,isLeader));
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
