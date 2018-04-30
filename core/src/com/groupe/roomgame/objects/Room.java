package com.groupe.roomgame.objects;

/*
 * @ author Dominic Dewhurst
 */

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Room extends Interactive {

	public Room(TiledMap map, World world, String id) {
		super(map, world, id);
	}
}
