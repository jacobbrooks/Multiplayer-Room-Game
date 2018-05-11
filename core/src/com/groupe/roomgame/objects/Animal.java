package com.groupe.roomgame.objects;

import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.groupe.roomgame.tools.BodyBuilder;
import com.groupe.roomgame.objects.Character;

import com.groupe.roomgame.screens.GameScreen;

public class Animal extends Character{

	public Animal(int id, float x, float y, World world) {
		super(id, x, y, world);
		sprite = new Sprite(new Texture("characters/dog"));
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
		sprite.setSize(1, 1);
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
		return ANIMAL;
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
			cleanRoom();
		return left;
	}

	private boolean leaveRoom(GameScreen screen, Room[] rooms){
		if (currentRoom.getRoomState() == Room.DIRTY){
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
		/* like clean rooms */
 		if (currentRoom.getRoomState() == 2)
			currentRoom.setRoomState(1);
	}

	public void dirtyRoom(){
		/* animal will never want to dirty a room */
	}


}
