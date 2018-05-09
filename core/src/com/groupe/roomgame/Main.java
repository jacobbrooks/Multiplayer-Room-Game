/*
	@author Dominic Dewhurst
*/

package com.groupe.roomgame;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.groupe.roomgame.screens.GameScreen;
import com.groupe.roomgame.networking.election.HoldElection;
import com.groupe.roomgame.networking.election.IPs;

public class Main extends Game {
	private SpriteBatch batch;

	public void create () {
		boolean isLeader = false;

		/*try {
			HoldElection election = new HoldElection(2703);
			isLeader = election.run();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(batch, isLeader));
	}

	public void render () {
		super.render();
	}

	public void dispose () {
		batch.dispose();
	}
}
