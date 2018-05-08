package com.groupe.roomgame.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class Room {

	private int id;
	private Rectangle rect;
	private Body body;

	public Room(int id, Rectangle rect, Body body) {
		this.id = id;
		this.rect = rect;
		this.body = body;
	}
	
	public int getID() {
		return id;
	}
	
	public boolean overlaps(Rectangle rect) {
		return this.rect.overlaps(rect);
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
