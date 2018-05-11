package com.groupe.roomgame.objects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.groupe.roomgame.tools.BodyBuilder;
import com.groupe.roomgame.tools.Constants;
import com.groupe.roomgame.screens.GameScreen;

import java.util.Random;

public abstract class Character{
	
	public static final int PC = 0;
	public static final int ANIMAL = 1;
	public static final int NPC = 2;
	
	protected Body body;
	protected Sprite sprite;
	protected int id;
	protected World world;
	protected byte cBits = Constants.PLAYER_BITS;
	protected byte mBits = Constants.NON_INTERACTIVE_BITS | Constants.PLAYER_BITS;
	protected Rectangle rect;
	protected int respect;

	protected float x;
	protected float y;

	protected Room currentRoom;

	protected Random rand;

	public Character(int id, float x, float y, World world){
		this.rect = new Rectangle(x, y, 64, 64);
		body = BodyBuilder.createBox(world, BodyType.DynamicBody, rect, cBits, mBits, this);
		this.id = id;
		this.world = world;
		respect = 40;
		this.x = x / 100;
		this.y = y / 100;
		this.rand = new Random();
	}
	
	public void setRoom(Room[] rooms) {
		for (Room r : rooms) {
			if (rect.overlaps(r.getRect())) {
				this.currentRoom = r;
			}
		}
	}

	public Room getRoom(){
		return currentRoom;
	}
	
	public void update(float x, float y, int respect) {
		this.x = x;
		this.y = y;
		this.respect = respect;
		this.rect.setPosition(x * 100, y * 100);
	}

	public abstract Body getBody();
	
	public abstract Sprite getSprite();
	
	public abstract void render(SpriteBatch sb);

	public abstract int getId();
	
	public abstract int getType();

	public abstract int getRespect();
	
	public abstract void setRespect(int respect);

	public abstract float getX();

	public abstract float getY();

	public abstract void cleanRoom();

	public abstract void dirtyRoom();

	public abstract boolean check(Room[] rooms, GameScreen screen);
	
}