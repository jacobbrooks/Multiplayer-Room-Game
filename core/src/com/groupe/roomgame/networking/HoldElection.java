package com.groupe.roomgame.networking;

import java.io.IOException;
import java.net.MulticastSocket;

public class HoldElection {

	private final int PORT = 2703;
	private final String HOST = "pi.cs.oswego.edu";
	
	public HoldElection() throws IOException {
		MulticastSocket socket = new MulticastSocket();	
	}
}
