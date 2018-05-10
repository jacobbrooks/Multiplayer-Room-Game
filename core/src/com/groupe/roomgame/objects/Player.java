package com.groupe.roomgame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character{
	
	public Player(int id, float x, float y, World world) {
		super(id, x, y, world);
		sprite = new Sprite(new Texture("characters/person.png"));
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
		return PC;
	}

	public int getRespect() {
		return respect;
	}
	
	public void setRespect(int respect) {
		this.respect = respect;
	}

	
}
