/*
	@author Dominic Dewhurst
*/

package com.groupe.roomgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.groupe.roomgame.networking.HoldElection;
import java.io.IOException;

public class Main extends ApplicationAdapter{
	SpriteBatch batch; 
	Texture img;

	public void create () {
		try {
			HoldElection election = new HoldElection();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.end();
	}

	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
