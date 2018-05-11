package com.groupe.roomgame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import com.groupe.roomgame.screens.GameScreen;

public class NPC extends Character{

	public NPC(int id, float x, float y, World world) {
		super(id, x, y, world);
		sprite = new Sprite(new Texture("characters/npc.png"));
	}

	public Body getBody() {
		return body;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void render(SpriteBatch sb) {
		sprite.setPosition((float)(body.getPosition().x - sprite.getWidth() / 2), (float)(body.getPosition().y - sprite.getHeight() / 2));
		sprite.setOriginCenter();
		sprite.setSize(.7f, .7f);
		sprite.draw(sb);
	}

	public void update(float x, float y){
		this.x = x;
		this.y = y;
		this.rect.setPosition(x * 100, y * 100);
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return NPC;
	}

	public int getRespect() {
		return respect;
	}

	public void setRespect(int respect) {
		this.respect = respect;
	}

	public boolean check(Room[] rooms, GameScreen screen){
		setRoom(rooms);
		boolean left = leaveRoom(screen, rooms);
		if (left)
			dirtyRoom();
		return left;
	}

	private boolean leaveRoom(GameScreen screen, Room[] rooms){
		if (currentRoom.getRoomState() == Room.CLEAN){
			int newRoomID = rand.nextInt(6);
			while (newRoomID == currentRoom.getID())
				newRoomID = rand.nextInt(6);

			float[] coords = screen.randomRoomCoordinates(false, newRoomID);
			update(coords[0], coords[1]);
			currentRoom = rooms[newRoomID];
			this.x = x / 100;
			this.y = y / 100;
			return true;
		}
		return false;
	}

	public void cleanRoom(){
		/* npc will never want to clean a room */
	}

	public void dirtyRoom(){
		/* like dirty rooms */
		if (currentRoom.getRoomState() == Room.CLEAN)
			currentRoom.setRoomState(1);
	}

}
