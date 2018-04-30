package com.groupe.roomgame.tools;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Constants {
	public static final float SCALE = 100f;

	public static final byte PLAYER_BITS = 1;
	public static final byte NON_INTERACTIVE_BITS = 2;
	public static final byte INTERACTIVE_BITS = 4;
	
	public static final BodyType DYNAMIC_BODY = BodyDef.BodyType.DynamicBody;
	public static final BodyType STATIC_BODY = BodyDef.BodyType.StaticBody;
}
