/*
	@author Dominic Dewhurst
*/

package com.groupe.roomgame;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.groupe.roomgame.networking.HoldElection;
import com.groupe.roomgame.networking.IPs;
import com.groupe.roomgame.screens.GameScreen;

public class Main extends Game {
	private SpriteBatch batch;

	public void create () {
		/*HoldElection election = new HoldElection();
		boolean isLeader = false;;
		
		try {
			isLeader = election.start();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(batch, false));
	}

	public void render () {
		super.render();
	}

	public void dispose () {
		batch.dispose();
	}
}
