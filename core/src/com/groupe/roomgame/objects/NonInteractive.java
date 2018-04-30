package com.groupe.roomgame.objects;

/*
 * @ author Dominic Dewhurst
*/

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.groupe.roomgame.tools.BodyBuilder;
import com.groupe.roomgame.tools.Constants;

public abstract class NonInteractive {
	public NonInteractive(TiledMap map, World world, String id) {
		for (MapObject object : map.getLayers().get(id).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			BodyBuilder.createBox(world, Constants.STATIC_BODY, rect, Constants.NON_INTERACTIVE_BITS, Constants.PLAYER_BITS, this);
		}
	}
}
