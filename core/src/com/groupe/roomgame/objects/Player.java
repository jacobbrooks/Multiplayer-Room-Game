package com.groupe.roomgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.groupe.roomgame.tools.BodyBuilder;
import com.groupe.roomgame.tools.Constants;

public class Player {
	
	private Body body;
	private World world;
	private Sprite sprite;
	
	public Player(World world) {
		this.world = world;
		body = BodyBuilder.createCircle(world, BodyType.DynamicBody, 350f, 350f, 30);
		sprite = new Sprite(new Texture("characters/person.png"));
	}
	
	public Body getBody() {
		return body;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void render(SpriteBatch sb) {
		body.setLinearVelocity(new Vector2(0f, 0f));
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			body.setLinearVelocity(new Vector2(-1f, 0f));
			sprite.setRotation((float) Math.toDegrees(Math.PI));
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			body.setLinearVelocity(new Vector2(1f, 0f));
			sprite.setRotation((float) 0f);
		}
		if(Gdx.input.isKeyPressed(Keys.UP)){
			body.setLinearVelocity(new Vector2(0f, 1f));
			sprite.setRotation((float) Math.toDegrees(Math.PI / 2));
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			body.setLinearVelocity(new Vector2(0f, -1f));
			sprite.setRotation((float) Math.toDegrees(3 * Math.PI / 2));
		}
		sprite.setSize(1, 1);
		sprite.setPosition((float)(body.getPosition().x - sprite.getWidth() / 2), (float)(body.getPosition().y - sprite.getWidth() / 2));
		sprite.setOriginCenter();
		sprite.setSize(1, 1);
		sprite.draw(sb);
		
	}

}
