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

public class Player {
	
	private Body body;
	private Sprite sprite;
	private int id;
	
	public Player(int id, ConcurrentHashMap<Integer, Player> gameState, float x, float y, World world) {
		this.id = id;
		this.body = BodyBuilder.createCircle(world, BodyType.DynamicBody, x, y, 30);
		this.sprite = new Sprite(new Texture("characters/person.png"));
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
	

	public void update(float dx, float dy){
		body.setTransform(body.getPosition().x + dx, body.getPosition().y + dy, body.getAngle());
	}

	
	public int getId() {
		return id;
	}

}
