package com.groupe.roomgame.desktop;

/*
	@author Dominic Dewhurst
*/

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.groupe.roomgame.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Main(), config);
		config.width = 800;
		config.height = 800;
		config.resizable = false;
	}
}
