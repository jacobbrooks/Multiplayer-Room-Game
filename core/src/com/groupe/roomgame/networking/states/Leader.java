package com.groupe.roomgame.networking.states;

/*
* @author Dominic Dewhurst
*/

import java.io.IOException;

import com.groupe.roomgame.networking.packets.ConfirmationPacket;

public class Leader {
	
	private int port;

	public Leader(){
		this.port = 2703;
	}

	public void sendConfirmations() throws IOException {
		ConfirmationPacket confirmationPacket = new ConfirmationPacket();
		System.out.println("I became leader.");

		for (int i = 0; i < IPs.getIPs.length; i++)
			new Sender(confirmationPacket, IPs.getIPs[i], port).start();

		System.out.println("Sent confirmationPackets");
		System.exit(0);
	}
}
