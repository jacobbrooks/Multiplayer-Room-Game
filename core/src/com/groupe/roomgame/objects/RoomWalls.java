package com.groupe.roomgame.objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class RoomWalls extends NonInteractive {

	public RoomWalls(TiledMap map, World world, String id) {
		super(map, world, id);
	}

}
