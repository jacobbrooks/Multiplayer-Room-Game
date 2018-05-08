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

public abstract class Character{

	protected Body body;
	protected Sprite sprite;
	protected int id;
	protected World world;

	public Character(int id, float x, float y, World world){
		body = BodyBuilder.createCircle(world, BodyType.DynamicBody, x, y, 30);
		this.id = id;
		this.world = world;
	}

	public abstract Body getBody();
	
	public abstract Sprite getSprite();
	
	public abstract void render(SpriteBatch sb);

	public abstract void update(float dx, float dy);

	public abstract int getId();

}