package com.groupe.roomgame.objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class Hallway extends NonInteractive {

	public Hallway(TiledMap map, World world, String id) {
		super(map, world, id);
	}

}
