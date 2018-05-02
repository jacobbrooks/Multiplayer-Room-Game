package com.groupe.roomgame.networking;

/*
* @author Dominic Dewhurst
*/

import java.io.IOException;

public class HoldElection {

	private static final int PORT = 2703;
	private static final int VOTE_PORT = 2704;
	
	public static void main(String[] args) throws IOException {
		IPs.set1();
		IPs.set2();
		IPs.set3();

		Follower follower = new Follower(PORT, VOTE_PORT);
		follower.run();
	}
}
