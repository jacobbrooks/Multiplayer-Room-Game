/*
	@author Dominic Dewhurst
*/

package com.groupe.roomgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.groupe.roomgame.screens.GameScreen;

public class Main extends Game {
	private SpriteBatch batch;

	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(batch));
	}

	public void render () {
		super.render();
	}

	public void dispose () {
		batch.dispose();
	}
}
