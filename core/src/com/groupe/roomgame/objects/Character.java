package com.groupe.roomgame.objects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.groupe.roomgame.tools.BodyBuilder;
import com.groupe.roomgame.tools.Constants;

public abstract class Character{
	
	public static final int PC = 0;
	public static final int ANIMAL = 1;
	public static final int NPC = 2;
	
	protected Body body;
	protected Sprite sprite;
	protected int id;
	protected World world;
	protected byte cBits = Constants.PLAYER_BITS;
	protected byte mBits = Constants.NON_INTERACTIVE_BITS;
	protected Rectangle rect;
	protected int respect;

	protected float x;
	protected float y;

	public Character(int id, float x, float y, World world){
		this.rect = new Rectangle(x, y, 64, 64);
		body = BodyBuilder.createBox(world, BodyType.DynamicBody, rect, cBits, mBits, this);
		this.id = id;
		this.world = world;
		respect = 40;
	}
	
	public void getRoom(Room[] rooms) {
		for (Room r : rooms) {
			if (rect.overlaps(r.getRect())) {
				System.out.println(r.getID());
				System.out.println("State: " + r.getRoomState());
			}
		}
	}
	
	public void update(float delta) {
		this.rect.setPosition(body.getPosition().x * 100, body.getPosition().y * 100);
	}

	public abstract Body getBody();
	
	public abstract Sprite getSprite();
	
	public abstract void render(SpriteBatch sb);

	public abstract void update(float dx, float dy);

	public abstract int getId();
	
	public abstract int getType();

	public abstract int getRespect();
	
	public abstract void setRespect(int respect);

	public abstract float getX();

	public abstract float getY();
	
}