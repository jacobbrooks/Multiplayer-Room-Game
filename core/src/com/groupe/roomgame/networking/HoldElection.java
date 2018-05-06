package com.groupe.roomgame.networking;

/*
* @author Dominic Dewhurst
*/

import java.io.IOException;

import com.groupe.roomgame.networking.states.Follower;

public class HoldElection {

	private static final int PORT = 2703;
	private static final int VOTE_PORT = 2704;
	
	public boolean start() throws IOException {
		IPs.set1();
		IPs.set2();
		IPs.set3();

		Follower follower = new Follower(PORT, VOTE_PORT);
		return(follower.run());
	}
}
