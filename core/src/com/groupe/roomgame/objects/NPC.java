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
		boolean left = leaveRoom(screen);
		screen.updateState();
		setRoom(rooms);
		dirtyRoom();
		return left;
	}

	private boolean leaveRoom(GameScreen screen){
		if (currentRoom.getRoomState() == 0){
			float[] coords = new float[2];
			int newRoomID = rand.nextInt();
			while (newRoomID == currentRoom.getID())
				newRoomID = rand.nextInt();

			coords = screen.randomRoomCoordinates(false, newRoomID);
			update(coords[0], coords[1]);
			return true;
		}
		return false;
	}

	public void cleanRoom(){
		/* npc will never want to clean a room */

	}

	public void dirtyRoom(){
		/* like dirty rooms */
		if (currentRoom.getRoomState() == 0)
			currentRoom.setRoomState(1);
	}

}
